package cache.memory;

import cache.Weigher;
import cache.memory.entry.CacheEntryFactory;
import cache.memory.entry.CacheEntryFactory.SoftCacheEntryFactory;
import cache.memory.entry.CacheEntryFactory.StrongCacheEntryFactory;
import cache.memory.entry.CacheEntryFactory.WeakCacheEntryFactory;

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
