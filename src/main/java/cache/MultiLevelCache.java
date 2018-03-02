package cache;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.tuple.Pair;
import util.Validate;

class MultiLevelCache<K, V extends Serializable> implements Cache<K, V> {

	private Weigher<K> weigher;
	private long maxWeight;
	private long delimeterWeight;
	private long minWeight;

	private Cache<K, V> currentLevelCache;
	private Cache<K, V> nextLevelCache;

	MultiLevelCache(Weigher<K> weigher, long maxWeight, long delimeterWeight, long minWeight,
		Cache<K, V> currentLevelCache, Cache<K, V> nextLevelCache) {
		Validate.check(weigher, "Weighter must be not null", Objects::nonNull);
		Validate.check(minWeight, delimeterWeight, "Weights must be correct", (min, max) -> min < max);
		Validate.check(delimeterWeight, maxWeight, "Weights must be correct", (min, max) -> min < max);
		Validate.check(minWeight, "Weights must be correct", min -> min > 0);
		Validate.check(nextLevelCache, "Caches must be not null", Objects::nonNull);
		Validate.check(currentLevelCache, "Caches must be not null", Objects::nonNull);

		this.weigher = weigher;
		this.maxWeight = maxWeight;
		this.delimeterWeight = delimeterWeight;
		this.minWeight = minWeight;
		this.currentLevelCache = currentLevelCache;
		this.nextLevelCache = nextLevelCache;
	}

	@Override
	public V get(K key) {
		Cache<K, V> cache = findCache(key);
		if (cache == null) {
			return null;
		}
		return cache.get(key);
	}

	@Override
	public Map<K, V> getAllPresent(Iterable<K> keys) {
		return StreamSupport.stream(keys.spliterator(), true)
			.map(key -> Pair.of(key, get(key)))
			.filter(kvPair -> kvPair.getRight() != null)
			.collect(Collectors.toMap(Pair::getKey, Pair::getValue));
	}

	@Override
	public void put(K key, V value) {
		Cache<K, V> cache = findCache(key);
		if (cache != null) {
			cache.put(key, value);
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		map.forEach(this::put);
	}

	@Override
	public void invalidate(K key) {
		Cache<K, V> cache = findCache(key);
		if (cache != null) {
			cache.invalidate(key);
		}
	}

	@Override
	public void invalidateAll(Iterable<K> keys) {
		StreamSupport.stream(keys.spliterator(), true)
			.forEach(this::invalidate);
	}

	@Override
	public void invalidateAll() {
		synchronized (this) {
			currentLevelCache.invalidateAll();
			nextLevelCache.invalidateAll();
		}
	}

	@Override
	public long size() {
		return currentLevelCache.size() + nextLevelCache.size();
	}

	@Override
	public ConcurrentMap<K, V> asMap() {
		ConcurrentMap<K, V> current = currentLevelCache.asMap();
		ConcurrentMap<K, V> next = nextLevelCache.asMap();
		current.putAll(next);

		return current;
	}


	private Cache<K, V> findCache(K key) {
		long weight = weigher.weight(key);
		if (minWeight <= weight && weight <= delimeterWeight) {
			return currentLevelCache;
		}
		if (delimeterWeight < weight && weight <= maxWeight) {
			return nextLevelCache;
		}
		return null;
	}


}
