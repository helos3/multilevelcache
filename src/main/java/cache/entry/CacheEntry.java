package cache.entry;

import java.util.Map.Entry;

public interface CacheEntry<K, V> {
	K getKey();
	V getValue();

	long getAccessTime();
	long getWriteTime();

	long getValueWeight();
	boolean isActive();

}
