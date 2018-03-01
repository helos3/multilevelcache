package cache;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

class FileChunk<K, V extends Serializable> {

	private FileChannel readChannel;
	private FileChannel writeChannel;
	private File file;

	private final int capacity;
	private final HashMap<K, ObjectData> objectsMetadata;

	FileChunk(String fileName, int capacity){
		try {
			this.file = Files.createTempFile(fileName, ".tmp")
				.toFile();
			this.file.deleteOnExit();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.capacity = capacity;
		this.objectsMetadata = new HashMap<>();
		try {
			init();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	private void closeChannels() throws IOException {
		if (readChannel != null) {
			readChannel.close();
		}
		if (writeChannel != null) {
			writeChannel.close();
		}
	}

	private void init() throws IOException {
		closeChannels();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		raf.setLength(0);
		readChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
		writeChannel = FileChannel.open(file.toPath(), StandardOpenOption.APPEND);
		objectsMetadata.clear();
	}

	public Map<K, V> asMap() {
		try (FileLock lock = writeChannel.lock()) {
			return objectsMetadata.entrySet()
				.stream()
				.collect(Collectors.toMap(Entry::getKey,
					entry -> nonSafeRead(entry.getValue())));
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	private V nonSafeRead(ObjectData metadata) {
		ByteBuffer buffer = ByteBuffer.allocate(metadata.length);
		try {
			readChannel.read(buffer, metadata.startPos);
		} catch (IOException e) {
			return null;
		}
		return SerializationUtils.deserialize(buffer.array());
	}

	public V get(K key) {
		ObjectData metadata = objectsMetadata.get(key);
		try (FileLock lock = writeChannel.lock()) {
			ByteBuffer buffer = ByteBuffer.allocate(metadata.length);
			readChannel.read(buffer, metadata.startPos);
			return SerializationUtils.deserialize(buffer.array());
		} catch (IOException e) {
			throw new SerializationException(e);
		}


	}

	public void put(K key, V value) {
		ByteBuffer buf = ByteBuffer.wrap(SerializationUtils.serialize(value));
		try (FileLock lock = writeChannel.lock()) {
			objectsMetadata.put(key, new ObjectData(writeChannel.position(), buf.capacity()));
			writeChannel.write(buf);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	public synchronized void invalidate(K key) {
		objectsMetadata.remove(key);
	}

	public synchronized void invalidateAll(){
		try {
			init();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int size() {
		return objectsMetadata.size();
	}

	private class ObjectData {

		final long startPos;
		final int length;

		public ObjectData(long startPos, int length) {
			this.startPos = startPos;
			this.length = length;
		}
	}

}
