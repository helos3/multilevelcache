package util;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Validate {


	public static <T> void check(T val, String msg, Predicate<T> checker) {
		if (!checker.test(val)) throw new IllegalStateException(msg);
	}

	public static <T, U> void check(T val1, U val2,  String msg, BiPredicate<T, U> checker) {
		if (!checker.test(val1, val2)) throw new IllegalStateException(msg);
	}

}
