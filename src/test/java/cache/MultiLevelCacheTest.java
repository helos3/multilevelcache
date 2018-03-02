package cache;

import cache.file.FileCacheBuilder;
import cache.memory.MemCacheBuilder;
import java.util.function.Supplier;
import java.util.stream.Stream;
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

	@Test
	public void narkomanTest() {
		Supplier<Cache<Integer, String>> cacheSupplier = () -> MemCacheBuilder.<Integer, String>newBuilder().build();

		MultiLevelCacheBuilder<Integer, String> init = MultiLevelCacheBuilder.<Integer, String>newBuilder()
			.minWeight(0)
			.delimeterWeight(20)
			.maxWeight(100)
			.keyWeigher(i -> i)
			.currentLevelCache(cacheSupplier.get());

		Cache<Integer, String> last = MultiLevelCacheBuilder.<Integer, String>newBuilder()
			.currentLevelCache(cacheSupplier.get())
			.keyWeigher(num -> num)
			.minWeight(80)
			.maxWeight(100)
			.delimeterWeight(90)
			.nextLevelCache(cacheSupplier.get())
			.currentLevelCache(cacheSupplier.get())
			.build();


		for (int i = 60; i >= 20; i = i - 20) {
			last = MultiLevelCacheBuilder.<Integer, String>newBuilder()
				.currentLevelCache(cacheSupplier.get())
				.keyWeigher(num -> num)
				.minWeight(i)
				.maxWeight(100)
				.delimeterWeight(i + 20)
				.nextLevelCache(last)
				.currentLevelCache(cacheSupplier.get())
				.build();
		}

		MultiLevelCache<Integer, String> narkomanCache = init.nextLevelCache(last)
			.build();
		Stream.iterate(1, i -> i + 1)
			.limit(99)
			.forEach(num -> narkomanCache.put(num, num.toString()));
		Assert.assertEquals(99 , narkomanCache.size());
	}


}
