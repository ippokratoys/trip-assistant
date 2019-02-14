package com.mantzavelas.tripassistantapi.utils;

public class PropertyUtil {

	private static PropertyLoader properties;

	private static void initialize() {
		if (properties == null) {
			properties = BeanUtil.getBean(PropertyLoader.class);
		}
	}

	public static String getProperty(String property) {
		initialize();
		return properties.getProperty(property);
	}
}
