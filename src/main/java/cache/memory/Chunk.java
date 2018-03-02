package cache.memory;

import cache.memory.entry.CacheEntry;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Chunk<K, V> {

	private final ReentrantLock lock = new ReentrantLock();
	private final AtomicReferenceArray<CacheEntry<K, V>> array;
	private final int capacity;
	private final int accessTimeInvalidity;
	private final int writeTimeInvalidity;
	private final AtomicInteger counter = new AtomicInteger(0);

	Chunk(int capacity, int accessTimeInvalidity, int writeTimeInvalidity) {
		this.capacity = capacity;
		this.accessTimeInvalidity = accessTimeInvalidity;
		this.writeTimeInvalidity = writeTimeInvalidity;
		array = new AtomicReferenceArray<>(capacity);
	}

	public List<CacheEntry<K, V>> asList() {
		lock.lock();
		List<CacheEntry<K, V>> result = Stream.iterate(1, i -> i + 1)
			.limit(capacity - 1)
			.map(this::getEntry)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		lock.unlock();
		return result;
	}

	public void put(int index, CacheEntry<K, V> entry) {
		if (entry == null)
			return;
		counter.incrementAndGet();
		update(index, entry);
	}

	public V get(int index) {
		CacheEntry<K, V> entry = getEntry(index);
		return entry == null ? null : entry.getValue();
	}

	public CacheEntry<K, V> getEntry(int index) {
		lock.lock();
		CacheEntry<K, V> found = array.get(index);
		CacheEntry<K, V> result = isValid(found) ? found : null;
		if (result == null)
			counter.decrementAndGet();
		array.lazySet(index, result);
		lock.unlock();
		return result;
	}

	public int size() {
		return counter.get();
	}

	public void invalidate(int index) {
		update(index, null);
		counter.decrementAndGet();
	}

	private void update(int index, CacheEntry<K, V> newVal) {
		lock.lock();
		array.lazySet(index, newVal);
		lock.unlock();
	}


	private boolean isValid(CacheEntry<K, V> entry) {
		if (entry == null) {
			return false;
		}
		long currentTime = System.currentTimeMillis();
		boolean timeValidity = (writeTimeInvalidity == -1
			|| currentTime - entry.getWriteTime() <= writeTimeInvalidity)
			&& (accessTimeInvalidity == -1
			|| currentTime - entry.getAccessTime() <= accessTimeInvalidity);
		return entry.isActive() && timeValidity;
	}
}
