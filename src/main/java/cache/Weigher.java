package cache;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface Weigher<V> {

	long weight(V value);

	Weigher NON_WEIGHT_WEIGHER = obj -> -1L;

}
