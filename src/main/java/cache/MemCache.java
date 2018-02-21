package cache;

import cache.entry.CacheEntryFactory;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class MemCache<K extends Serializable, V extends Serializable> implements Cache<K,V> {

	private Chunk<K, V>[] chunks;
	private boolean weakEntries;
	private long expireAfterAccess;
	private long expireAfterWrite;
	private CacheEntryFactory<K, V> cacheEntryFactory;
	private long maxWeight;
	private long minWeight;
	private int maximumSize;


	@Override
	public V get(K key) {
		return null;
	}

	@Override
	public Map<K, V> getAllPresent(Iterable<?> keys) {
		return null;
	}

	@Override
	public void put(K key, V value) {

	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {

	}

	@Override
	public void invalidate(Object key) {

	}

	@Override
	public void invalidateAll(Iterable<?> keys) {

	}

	@Override
	public void invalidateAll() {

	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public ConcurrentMap<K, V> asMap() {
		return null;
	}
}
