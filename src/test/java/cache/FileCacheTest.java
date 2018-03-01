package cache;

import static org.junit.Assert.*;

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
}