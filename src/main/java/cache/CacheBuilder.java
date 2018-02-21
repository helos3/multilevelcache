package cache;

public class CacheBuilder<K,V> {
	boolean weakEntries;
	long expireAfterAccess;
	long expireAfterWrite;
	Weigher<V> weigher;
	long maxWeight;
	long minWeight;
	int maximumSize;

	public static <K,V> CacheBuilder<K,V> newBuilder() {
		return new CacheBuilder<>();
	}

	public CacheBuilder<K, V> setWeakEntries(boolean weakEntries) {
		this.weakEntries = weakEntries;
		return this;
	}

	public CacheBuilder<K, V> setExpireAfterAccess(long expireAfterAccess) {
		this.expireAfterAccess = expireAfterAccess;
		return this;
	}

	public CacheBuilder<K, V> setExpireAfterWrite(long expireAfterWrite) {
		this.expireAfterWrite = expireAfterWrite;
		return this;
	}

	public CacheBuilder<K, V> setWeigher(Weigher<V> weigher) {
		this.weigher = weigher;
		return this;
	}

	public CacheBuilder<K, V> setMaxWeight(long maxWeight) {
		this.maxWeight = maxWeight;
		return this;
	}

	public CacheBuilder<K, V> setMinWeight(long minWeight) {
		this.minWeight = minWeight;
		return this;
	}

	public CacheBuilder<K, V> setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
		return this;
	}


}
