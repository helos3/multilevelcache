package cache.memory;

import cache.Cache;
import cache.memory.entry.CacheEntry;
import cache.memory.entry.CacheEntryFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class MemCache<K, V> implements Cache<K, V> {

	private Chunk<K, V>[] chunks;
	private final CacheEntryFactory<K, V> cacheEntryFactory;

	private final long maxWeight;
	private final long minWeight;

	private final int numberOfChunks;
	private final int expireAfterAccess;
	private final int expireAfterWrite;
	private final int maximumSize;


	MemCache(int expireAfterAccess, int expireAfterWrite,
		long maxWeight,
		long minWeight, int maximumSize, CacheEntryFactory cacheEntryFactory, int numberOfChunks) {
		this.maxWeight = maxWeight;
		this.minWeight = minWeight;
		this.cacheEntryFactory = cacheEntryFactory;
		this.numberOfChunks = numberOfChunks;
		this.expireAfterAccess = expireAfterAccess;
		this.expireAfterWrite = expireAfterWrite;
		this.maximumSize = maximumSize;
		init();
	}

	private void init() {
		chunks = new Chunk[numberOfChunks];
		for (int i = 0; i < numberOfChunks; i++) {
			chunks[i] = new Chunk<>(maximumSize / numberOfChunks, expireAfterAccess,
				expireAfterWrite);
		}
	}

	@Override
	public V get(K key) {
		return findChunk(key).get(index(key));

	}

	@Override
	public Map<K, V> getAllPresent(Iterable<K> keys) {
		return StreamSupport.stream(keys.spliterator(), true)
			.map(k -> findChunk(k).getEntry(index(k)))
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(CacheEntry::getKey, CacheEntry::getValue));
	}

	@Override
	public void put(K key, V value) {
		_put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		map.entrySet()
			.stream()
			.parallel()
			.forEach(entry -> _put(entry.getKey(), entry.getValue()));
	}

	@Override
	public void invalidate(K key) {
		findChunk(key).invalidate(index(key));
	}

	@Override
	public void invalidateAll(Iterable<K> keys) {
		StreamSupport.stream(keys.spliterator(), true)
			.forEach(k -> findChunk(k).invalidate(index(k)));
	}

	@Override
	public void invalidateAll() {
		init();
	}

	@Override
	public long size() {
		return Stream.of(chunks)
			.map(Chunk::size)
			.reduce(Integer::sum)
			.orElse(0);
	}

	@Override
	public ConcurrentMap<K, V> asMap() {
		return Stream.of(chunks)
			.parallel()
			.map(Chunk::asList)
			.flatMap(List::stream)
			.collect(Collectors.toConcurrentMap(CacheEntry::getKey, CacheEntry::getValue));
	}

	private Chunk<K, V> findChunk(K key) {
		int hash = key.hashCode();
		return chunks[hash % numberOfChunks];
	}

	private int index(K key) {
		int hash = key.hashCode();
		return hash / numberOfChunks;
	}

	private boolean checkWeight(CacheEntry<K, V> entry) {
		return (maxWeight == -1) || minWeight <= entry.getValueWeight()
			&& entry.getValueWeight() >= maxWeight;
	}

	private void _put(K key, V val) {
		CacheEntry<K, V> entry = cacheEntryFactory.createEntry(key, val);
		if (!checkWeight(entry)) {
			return;
		}
		findChunk(key).put(index(key), entry);
	}


}
