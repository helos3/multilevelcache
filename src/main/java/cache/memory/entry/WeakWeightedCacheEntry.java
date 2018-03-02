package cache.memory.entry;

import java.lang.ref.WeakReference;

public class WeakWeightedCacheEntry<K, V> extends AbstractWeightedCacheEntry<K, V> {

	private final WeakReference<K> weakKey;
	private final V value;

	private final long initialValueWeight;


	public static <K,V> WeakWeightedCacheEntry<K, V> of(K key, V value, long weight) {
		return new WeakWeightedCacheEntry<>(key, value, weight);
	}

	private WeakWeightedCacheEntry(K key, V value, long weight) {
		super(null);
		initialValueWeight = weight;
		this.value = value;
		this.weakKey = new WeakReference<>(key);
		updateAccessTime();
	}

	@Override
	public V getValue() {
		updateAccessTime();
		return value;
	}

	@Override
	public boolean isActive() {
		updateAccessTime();
		return weakKey != null;
	}

	@Override
	public K getKey() {
		updateAccessTime();
		return weakKey.get();
	}

	@Override
	public long getValueWeight() {
		return isActive() ? initialValueWeight : -1L;
	}

}
