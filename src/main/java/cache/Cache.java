package cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

public interface Cache<K extends Serializable, V extends Serializable> {
	V get(K key);

	Map<K, V> getAllPresent(Iterable<?> keys);

	void put(K key, V value);

	void putAll(Map<? extends K, ? extends V> m);

	void invalidate(Object key);

	void invalidateAll(Iterable<?> keys);

	void invalidateAll();

	long size();

	ConcurrentMap<K, V> asMap();

}
