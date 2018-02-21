package cache.entry;

public abstract class AbstractWeightedCacheEntry<K, V> implements CacheEntry<K, V> {

	protected final K key;

	protected final long writeTime;
	protected long accessTime;


	public AbstractWeightedCacheEntry(K key) {
		this.key = key;
		writeTime = System.nanoTime();
	}

	protected void updateAccessTime() {
		accessTime = System.nanoTime();
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public long getAccessTime() {
		return accessTime;
	}

	@Override
	public long getWriteTime() {
		return writeTime;
	}
}
