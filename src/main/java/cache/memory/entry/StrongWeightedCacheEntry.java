package cache.memory.entry;

class StrongWeightedCacheEntry<K, V> extends AbstractWeightedCacheEntry<K, V> {
	private final V value;
	private final long valueWeight;

	public static <K,V> StrongWeightedCacheEntry<K, V> of(K key, V value, long weight) {
		return new StrongWeightedCacheEntry<>(key, value, weight);
	}

	private StrongWeightedCacheEntry(K key, V value, long weight) {
		super(key);
		valueWeight = weight;
		this.value = value;
	}

	@Override
	public V getValue() {
		updateAccessTime();
		return value;
	}

	@Override
	public boolean isActive() {
		updateAccessTime();
		return true;
	}

	@Override
	public long getValueWeight() {
		return valueWeight;
	}

}
