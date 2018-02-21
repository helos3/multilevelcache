package util;

import java.util.function.Predicate;

public class Validate {


	public static <T> T check(T val, String msg, Predicate<T> checker) {
		if (!checker.test(val)) throw new IllegalStateException(msg);
		return val;
	}
}
