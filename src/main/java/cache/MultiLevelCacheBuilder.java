package cache;

import java.io.Serializable;
import java.util.Objects;
import util.Validate;

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
		Validate.check(keyWeigher, "Weighter must be not null", Objects::nonNull);
		Validate.check(minWeight, delimeterWeight, "Weights must be correct", (min, max) -> min < max);
		Validate.check(delimeterWeight, maxWeight, "Weights must be correct", (min, max) -> min < max);
		Validate.check(minWeight, "Weights must be correct", min -> min > 0);
		Validate.check(nextLevelCache, "Caches must be not null", Objects::nonNull);
		Validate.check(currentLevelCache, "Caches must be not null", Objects::nonNull);

		return new MultiLevelCache<>(keyWeigher, maxWeight, delimeterWeight, minWeight, currentLevelCache, nextLevelCache);
	}
}
