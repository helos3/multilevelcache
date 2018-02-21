package cache;

public class MultilevelCacheBuilder<K,V> {

	private CacheBuilder<K,V> currentLevelCacheBuilder;

	private MultilevelCacheBuilder(CacheBuilder<K, V> currentLevelCacheBuilder) {
		this.currentLevelCacheBuilder = currentLevelCacheBuilder;
	}

	public static <K,V> MultilevelCacheBuilder of(CacheBuilder<K,V> builder) {
		return new MultilevelCacheBuilder<>(builder);
	}


}
