package cache;

import cache.entry.CacheEntryFactory;
import cache.entry.CacheEntryFactory.SoftCacheEntryFactory;
import cache.entry.CacheEntryFactory.StrongCacheEntryFactory;
import cache.entry.CacheEntryFactory.WeakCacheEntryFactory;

public enum CacheEntryType {
	STRONG {
		@Override
		<K, V> CacheEntryFactory<K, V> createFactory(Weigher<V> weigher) {
			return new StrongCacheEntryFactory<>(weigher);
		}
	}, WEAK_KEYS {
		@Override
		<K, V> CacheEntryFactory<K, V> createFactory(Weigher<V> weigher) {
			return new WeakCacheEntryFactory<>(weigher);
		}
	}, SOFT_VALUES {
		@Override
		<K, V> CacheEntryFactory<K, V> createFactory(Weigher<V> weigher) {
			return new SoftCacheEntryFactory<>(weigher);
		}
	};

	abstract <K,V> CacheEntryFactory<K,V> createFactory(Weigher<V> weigher);
}
