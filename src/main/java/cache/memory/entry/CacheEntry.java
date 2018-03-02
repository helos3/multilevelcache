package cache.memory.entry;

public interface CacheEntry<K, V> {
	K getKey();
	V getValue();

	long getAccessTime();
	long getWriteTime();

	long getValueWeight();
	boolean isActive();

}
