package cache;

import java.io.Serializable;

public class MultiLevelCacheBuilder<K, V extends Serializable> {

	private Weigher<K> keyWeigher;
	private long maxWeight;
	private long delimeterWeight;
	private long minWeight;
	private int maximumSize;

	private Cache<K, V> currentLevelCache;
	private Cache<K, V> nextLevelCache;


	public static <K, V extends Serializable> MultiLevelCacheBuilder<K, V> newBuilder() {
		return new MultiLevelCacheBuilder<>();
	}

	public MultiLevelCacheBuilder<K, V> keyWeigher(Weigher<K> keyWeigher) {
		this.keyWeigher = keyWeigher;
		return this;
	}

	public MultiLevelCacheBuilder<K, V> maxWeight(long maxWeight) {
		this.maxWeight = maxWeight;
		return this;
	}

	public MultiLevelCacheBuilder<K, V> delimeterWeight(long delimeterWeight) {
		this.delimeterWeight = delimeterWeight;
		return this;
	}

	public MultiLevelCacheBuilder<K, V> minWeight(long minWeight) {
		this.minWeight = minWeight;
		return this;
	}

	public MultiLevelCacheBuilder<K, V> maximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
		return this;
	}

	public MultiLevelCacheBuilder<K, V> currentLevelCache(Cache<K, V> currentLevelCache) {
		this.currentLevelCache = currentLevelCache;
		return this;
	}

	public MultiLevelCacheBuilder<K, V> nextLevelCache(Cache<K, V> nextLevelCache) {
		this.nextLevelCache = nextLevelCache;
		return this;
	}

	public MultiLevelCache<K,V> build() {
		return new MultiLevelCache<>(keyWeigher, maxWeight, delimeterWeight, minWeight, currentLevelCache, nextLevelCache);
	}
}
