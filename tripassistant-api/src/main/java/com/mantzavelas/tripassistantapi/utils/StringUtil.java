package com.mantzavelas.tripassistantapi.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {

	private StringUtil() { }

	public static String removeSequentialDuplicateChars(String character, String fileName) {
		String[] splitted = fileName.split(character);

		return Arrays.stream(splitted)
			.filter(s -> !StringUtil.empty(s))
			.collect(Collectors.joining("."));
	}

	public static boolean empty(String string) {
		return string == null || string.isEmpty();
	}

	public static String removeQuotes(String string) {
		if (string.startsWith("\"") && string.endsWith("\"")) {
			string = Arrays.stream(string.split("\""))
						   .filter(s -> !empty(s))
					 	   .findFirst()
					 	   .orElse("");
		}

		return string;
	}
}
