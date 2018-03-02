package cache;

import cache.file.FileCacheBuilder;
import cache.memory.MemCacheBuilder;
import org.junit.Assert;
import org.junit.Test;

public class MultiLevelCacheTest {

	@Test
	public void testInit() {

		MultiLevelCacheBuilder.<Integer, String>newBuilder()
			.keyWeigher(Integer::valueOf)
			.currentLevelCache(MemCacheBuilder.<Integer, String>newBuilder().build())
			.nextLevelCache(MemCacheBuilder.<Integer, String>newBuilder().build())
			.maxWeight(10)
			.minWeight(1)
			.delimeterWeight(5)
			.build();
	}


	@Test(expected = IllegalStateException.class)
	public void testFailureInit() {

		MultiLevelCacheBuilder.<Integer, String>newBuilder()
			.keyWeigher(Integer::valueOf)
			.currentLevelCache(MemCacheBuilder.<Integer, String>newBuilder().build())
			.nextLevelCache(MemCacheBuilder.<Integer, String>newBuilder().build())
			.maxWeight(10)
			.minWeight(1)
			.build();
	}


	@Test
	public void testMultilevelCache() {

		Cache<Integer, String> memCache = MemCacheBuilder.<Integer, String>newBuilder().build();
		Cache<Integer, String> fileCache = FileCacheBuilder.<Integer, String>newBuilder().fileNames(
			"temp")
			.build();

		MultiLevelCache<Integer, String> multiLevelCache = MultiLevelCacheBuilder.<Integer, String>newBuilder()
			.keyWeigher(Integer::valueOf)
			.currentLevelCache(memCache)
			.nextLevelCache(fileCache)
			.maxWeight(10)
			.minWeight(1)
			.delimeterWeight(5)
			.build();

		multiLevelCache.put(1, "ONE");
		multiLevelCache.put(3, "THREE");
		multiLevelCache.put(5, "FIVE");
		multiLevelCache.put(7, "SEVEN");
		multiLevelCache.put(9, "NINE");
		multiLevelCache.put(10, "TEN");

		Assert.assertEquals(3, memCache.size());
		Assert.assertEquals(3, fileCache.size());



	}


}
