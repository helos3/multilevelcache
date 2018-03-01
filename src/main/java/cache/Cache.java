package cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public interface Cache<K, V> {
	V get(K key);

	Map<K, V> getAllPresent(Iterable<K> keys);

	void put(K key, V value);

	void putAll(Map<? extends K, ? extends V> map);

	void invalidate(K key);

	void invalidateAll(Iterable<K> keys);

	void invalidateAll();

	long size();

	ConcurrentMap<K, V> asMap();

}
