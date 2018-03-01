package cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.Supplier;
import org.apache.commons.lang3.RandomStringUtils;

public class FileCacheBuilder<K, V extends Serializable> {

	private Weigher<V> weigher = Weigher.NON_WEIGHT_WEIGHER;
	private long maxWeight = -1;
	private long minWeight = -1;
	private int maximumSize = 10000;
	private String[] fileNames;

	private FileCacheBuilder() {
	}

	public static <K,V extends Serializable> FileCacheBuilder<K,V> newBuilder() {

		return new FileCacheBuilder<>();
	}

	public FileCacheBuilder<K, V> fileNames(String... fileNames) {
		this.fileNames = fileNames;
		return this;
	}

	public FileCacheBuilder<K, V> weigher(Weigher<V> weigher) {
		this.weigher = weigher;
		return this;
	}

	public FileCacheBuilder<K, V> maxWeight(long maxWeight) {
		this.maxWeight = maxWeight;
		return this;
	}

	public FileCacheBuilder<K, V> minWeight(long minWeight) {
		this.minWeight = minWeight;
		return this;
	}

	public FileCacheBuilder<K, V> maximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
		return this;
	}

	public Cache<K, V> build() {
		return new FileCache<>(weigher, maxWeight, minWeight, maximumSize, fileNames);
	}

}
