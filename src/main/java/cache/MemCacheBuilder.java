package cache;

import util.Validate;

public class MemCacheBuilder<K, V> {

	private int expireAfterAccess = -1;
	private int expireAfterWrite = -1;
	private Weigher<V> weigher = Weigher.NON_WEIGHT_WEIGHER;
	private long maxWeight = -1;
	private long minWeight = -1;
	private int maximumSize = 100;
	private CacheEntryType entryType = CacheEntryType.STRONG;
	private int numberOfChunks = 1;

	private MemCacheBuilder() {
	}

	public static <K, V> MemCacheBuilder<K, V> newBuilder() {
		return new MemCacheBuilder<>();
	}

	public MemCacheBuilder<K, V> expireAfterAccess(int expireAfterAccess) {
		Validate.check(expireAfterAccess, "Access time time must be > 1", val -> val > 1);
		this.expireAfterAccess = expireAfterAccess;
		return this;
	}

	public MemCacheBuilder<K, V> expireAfterWrite(int expireAfterWrite) {
		Validate.check(expireAfterWrite, "Write time must be > 1", val -> val > 1);
		this.expireAfterWrite = expireAfterWrite;
		return this;
	}

	public MemCacheBuilder<K, V> weigher(Weigher<V> weigher) {
		this.weigher = weigher;
		return this;
	}

	public MemCacheBuilder<K, V> maxWeight(long maxWeight) {
		this.maxWeight = maxWeight;
		return this;
	}

	public MemCacheBuilder<K, V> minWeight(long minWeight) {
		this.minWeight = minWeight;
		return this;
	}

	public MemCacheBuilder<K, V> maximumSize(int maximumSize) {
		Validate.check(maximumSize, "Maximum size must be > 10", val -> val > 10);
		this.maximumSize = maximumSize;
		return this;
	}

	public MemCacheBuilder<K, V> entryType(CacheEntryType entryType) {
		this.entryType = entryType;
		return this;
	}

	public MemCacheBuilder<K, V> numberOfChunks(int numberOfChunks) {
		this.numberOfChunks = numberOfChunks;
		return this;
	}

	public Cache<K, V> build() {
		return new MemCache<>(expireAfterAccess, expireAfterWrite, weigher, maxWeight,
			minWeight, maximumSize, entryType, numberOfChunks);
	}
}
