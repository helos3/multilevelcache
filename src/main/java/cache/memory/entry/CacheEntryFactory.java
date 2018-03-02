package cache.memory.entry;

import cache.Weigher;

public abstract class CacheEntryFactory<K, V> {
	protected final Weigher<V> weigher;

	protected CacheEntryFactory(Weigher<V> weigher) {
		this.weigher = weigher;
	}

	public abstract CacheEntry<K, V> createEntry(K key, V value);



	public static class WeakCacheEntryFactory<K,V> extends CacheEntryFactory<K,V> {
		public WeakCacheEntryFactory(Weigher<V> weigher) {
			super(weigher);
		}
		@Override
		public CacheEntry<K, V> createEntry(K key, V value) {
			return WeakWeightedCacheEntry.of(key, value, weigher.weight(value));
		}
	}

	public static class StrongCacheEntryFactory<K,V> extends CacheEntryFactory<K,V> {
		public StrongCacheEntryFactory(Weigher<V> weigher) {
			super(weigher);
		}
		@Override
		public CacheEntry<K, V> createEntry(K key, V value) {
			return StrongWeightedCacheEntry.of(key, value, weigher.weight(value));
		}
	}

	public static class SoftCacheEntryFactory<K,V> extends CacheEntryFactory<K,V> {
		public SoftCacheEntryFactory(Weigher<V> weigher) {
			super(weigher);
		}
		@Override
		public CacheEntry<K, V> createEntry(K key, V value) {
			return SoftWeightedCacheEntry.of(key, value, weigher.weight(value));
		}
	}

}
