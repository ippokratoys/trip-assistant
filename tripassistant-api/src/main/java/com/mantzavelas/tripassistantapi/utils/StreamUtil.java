package com.mantzavelas.tripassistantapi.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamUtil {

	private StreamUtil() { }

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> key) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(key.apply(t), Boolean.TRUE) == null;
	}
}
