package cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import util.Validate;

class FileCache<K, V extends Serializable> implements Cache<K, V> {

	FileChunk<K, V>[] chunks;

	private final int numberOfFiles;

	private final Weigher<V> weigher;
	private final long maxWeight;
	private final long minWeight;
	private final int maximumSize;

	FileCache(Weigher<V> weigher, long maxWeight, long minWeight, int maximumSize,
		String[] fileNames) {
		Validate.check(fileNames, "File name mustn't be null",
			array -> array != null && array.length != 0);
		this.weigher = weigher;
		this.maxWeight = maxWeight;
		this.minWeight = minWeight;
		this.maximumSize = maximumSize;
		this.numberOfFiles = fileNames.length;

		chunks = new FileChunk[numberOfFiles];
		for (int i = 0; i < fileNames.length; i++) {
			chunks[i] = new FileChunk<>(fileNames[i], maximumSize / numberOfFiles);
		}
	}


	@Override
	public V get(K key) {
		return findChunk(key).get(key);
	}

	@Override
	public Map<K, V> getAllPresent(Iterable<K> keys) {
		return StreamSupport.stream(keys.spliterator(), true)
			.collect(Collectors.toMap(key -> key, key -> findChunk(key).get(key)));

	}

	@Override
	public void put(K key, V value) {
		if(!validWeight(value)) {
			return;
		}
		_put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		map.entrySet()
			.stream()
			.parallel()
			.filter(entry -> validWeight(entry.getValue()))
			.forEach(entry -> put(entry.getKey(), entry.getValue()));

	}

	@Override
	public void invalidate(K key) {
		findChunk(key).invalidate(key);
	}

	@Override
	public void invalidateAll(Iterable<K> keys) {
		StreamSupport.stream(keys.spliterator(), true)
			.forEach(k -> findChunk(k).invalidate(k));
	}

	@Override
	public void invalidateAll() {
		for (FileChunk<K, V> chunk : chunks) {
			chunk.invalidateAll();
		}

	}

	@Override
	public long size() {
		return Stream.of(chunks)
			.map(FileChunk::size)
			.reduce(Integer::sum)
			.orElse(0);
	}

	@Override
	public ConcurrentMap<K, V> asMap() {
		return new ConcurrentHashMap<>(Stream.of(chunks)
			.map(FileChunk::asMap)
			.reduce((map1, map2) -> {
				map1.putAll(map2);
				return map1;
			})
			.orElse(new HashMap<>()));
	}

	private void _put(K key, V value) {
		findChunk(key).put(key, value);
	}

	private FileChunk<K, V> findChunk(K key) {
		int hash = key.hashCode();
		return chunks[hash % numberOfFiles];
	}

	private boolean validWeight(V val) {
		long weight = weigher.weight(val);
		return (maxWeight == -1) || minWeight <= weight
			&& weight >= maxWeight;
	}

}
