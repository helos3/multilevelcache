package cache.memory.entry;

import java.lang.ref.SoftReference;

class SoftWeightedCacheEntry<K, V> extends AbstractWeightedCacheEntry<K, V> {


	private final SoftReference<V> value;
	private final long initialValueWeight;


	public static <K,V> SoftWeightedCacheEntry<K, V> of(K key, V value, long weight) {
		return new SoftWeightedCacheEntry<>(key, value, weight);
	}

	private SoftWeightedCacheEntry(K key, V value, long weight) {
		super(key);
		initialValueWeight = weight;
		this.value = new SoftReference<>(value);
		updateAccessTime();
	}

	@Override
	public V getValue() {
		updateAccessTime();
		return value.get();
	}

	@Override
	public boolean isActive() {
		updateAccessTime();
		return value.get() != null;
	}

	@Override
	public long getValueWeight() {
		return isActive() ? initialValueWeight : -1L;
	}

}
