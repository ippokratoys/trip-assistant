package com.mantzavelas.tripassistantapi.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {

	@Test
	public void removeSequentialDuplicateChars() {
		String fileName = "abcd.jpeg";

		String actual = StringUtil.removeSequentialDuplicateChars("\\.", fileName);
		assertEquals(fileName, actual);
	}

	@Test
	public void removeSequentialDuplicateCharsMultipleDots() {
		String fileName = "abcd.a.b.jpeg";

		String actual = StringUtil.removeSequentialDuplicateChars("\\.", fileName);
		assertEquals(fileName, actual);
	}

	@Test
	public void removeSequentialDuplicateCharsSequentialDots() {
		String fileName1 = "abcd.a.b..jpeg";
		String expected1 = "abcd.a.b.jpeg";
		String fileName2 = "abcd.a.b....jpeg";
		String expected2 = "abcd.a.b.jpeg";

		String actual1 = StringUtil.removeSequentialDuplicateChars("\\.", fileName1);
		String actual2 = StringUtil.removeSequentialDuplicateChars("\\.", fileName2);

		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}

	@Test
	public void removeSequentialDuplicateCharsStartingEndingDot() {
		String fileName1 = "...abcd.a.b..jpeg";
		String expected1 = "abcd.a.b.jpeg";
		String fileName2 = "abcd.a.b....jpeg.....";
		String expected2 = "abcd.a.b.jpeg";

		String actual1 = StringUtil.removeSequentialDuplicateChars("\\.", fileName1);
		String actual2 = StringUtil.removeSequentialDuplicateChars("\\.", fileName2);

		assertEquals(expected1, actual1);
		assertEquals(expected2, actual2);
	}
}