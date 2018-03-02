package cache;

import cache.file.FileCacheBuilder;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import org.junit.Assert;
import org.junit.Test;

public class FileCacheTest {

	@Test
	public void initTest() {
		FileCacheBuilder.<Integer, String>newBuilder().fileNames("temp").build();
	}

	@Test
	public void fileCacheTest() {
		Cache<Integer, String> cache = FileCacheBuilder.<Integer, String>newBuilder().fileNames(
			"temp")
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
	public void memCacheManualInvalidationTest() throws Exception {
		Cache<Integer, String> cache = FileCacheBuilder.<Integer, String>newBuilder()
			.fileNames("temp")
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