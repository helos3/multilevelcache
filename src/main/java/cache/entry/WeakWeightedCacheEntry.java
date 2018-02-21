package cache.entry;

import java.lang.ref.WeakReference;

public class WeakWeightedCacheEntry<K, V> extends AbstractWeightedCacheEntry<K, V> {

	private final WeakReference<V> value;
	private final long initialValueWeight;


	public static <K,V> WeakWeightedCacheEntry<K, V> of(K key, V value, long weight) {
		return new WeakWeightedCacheEntry<>(key, value, weight);
	}

	private WeakWeightedCacheEntry(K key, V value, long weight) {
		super(key);
		initialValueWeight = weight;
		this.value = new WeakReference<>(value);
	}

	@Override
	public V getValue() {
		return value.get();
	}

	@Override
	public boolean isActive() {
		return value.get() != null;
	}

	@Override
	public long getValueWeight() {
		return isActive() ? initialValueWeight : -1L;
	}

}
