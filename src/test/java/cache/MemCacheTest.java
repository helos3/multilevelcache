package cache;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import org.junit.Assert;
import org.junit.Test;

public class MemCacheTest {

	@Test
	public void initTest() {
		MemCacheBuilder.newBuilder()
			.build();

		MemCacheBuilder.newBuilder()
			.entryType(CacheEntryType.STRONG)
			.expireAfterAccess(10)
			.expireAfterWrite(10)
			.build();

		MemCacheBuilder.newBuilder()
			.entryType(CacheEntryType.STRONG)
			.maximumSize(15)
			.build();

	}

	@Test
	public void memcacheTest() {
		Cache<Integer, String> cache = MemCacheBuilder.<Integer, String>newBuilder()
			.build();

		cache.put(1, "ONE");
		cache.put(2, "TWO");
		cache.put(3, "THREE");

		ConcurrentMap<Integer, String> values = cache.asMap();

		Assert.assertEquals(values.get(1), "ONE");
		Assert.assertEquals(values.get(2), "TWO");
		Assert.assertEquals(values.get(3), "THREE");
	}

	@Test
	public void memCacheWriteInvalidationTest() throws Exception {
		Cache<Integer, String> expireAfterWriteCache = MemCacheBuilder.<Integer, String>newBuilder()
			.expireAfterWrite(1000)
			.build();

		expireAfterWriteCache.put(1, "ONE");
		expireAfterWriteCache.put(2, "TWO");
		expireAfterWriteCache.put(3, "THREE");

		Assert.assertEquals(3, expireAfterWriteCache.asMap().size());


		Thread.sleep(1000);

		ConcurrentMap<Integer, String> values = expireAfterWriteCache.asMap();

		Assert.assertEquals(0, values.size());
		Assert.assertNull(expireAfterWriteCache.get(1));

		expireAfterWriteCache.put(4, "FOUR");
		Assert.assertEquals("FOUR", expireAfterWriteCache.get(4));

	}

	@Test
	public void memCacheAccessInvalidationTest() throws Exception {
		Cache<Integer, String> expireAfterAccessCache = MemCacheBuilder.<Integer, String>newBuilder()
			.expireAfterAccess(1000)
			.build();

		expireAfterAccessCache.put(1, "ONE");
		expireAfterAccessCache.put(2, "TWO");
		expireAfterAccessCache.put(3, "THREE");

		Thread.sleep(500);

		expireAfterAccessCache.get(2);

		Thread.sleep(500);

		ConcurrentMap<Integer, String> values = expireAfterAccessCache.asMap();

		Assert.assertEquals(values.size(), 1);
		Assert.assertEquals(expireAfterAccessCache.get(2), "TWO");

		expireAfterAccessCache.put(4, "FOUR");
		Assert.assertEquals(expireAfterAccessCache.get(4), "FOUR");

	}

	@Test
	public void memCacheWeightTest() throws Exception {
		Cache<Integer, String> weightCache = MemCacheBuilder.<Integer, String>newBuilder()
			.weigher(String::length)
			.maxWeight(5)
			.minWeight(2)
			.build();

		weightCache.put(1, "ON");
		weightCache.put(2, "TWO");
		weightCache.put(3, "THREEE");


		ConcurrentMap<Integer, String> values = weightCache.asMap();

		Assert.assertEquals(1, values.size());

	}

	@Test
	public void memCacheManualInvalidationTest() throws Exception {
		Cache<Integer, String> cache = MemCacheBuilder.<Integer, String>newBuilder()
			.build();

		cache.put(1, "ON");
		cache.put(2, "TWO");
		cache.put(3, "THREE");
		cache.put(4, "FOUR");

		cache.invalidate(1);

		Assert.assertEquals(3, cache.size());

		cache.invalidateAll(new ArrayList<Integer>() {{add(2); add(3);}});

		Assert.assertEquals(1, cache.size());

		cache.invalidateAll();

		Assert.assertEquals(0, cache.size());

	}



}