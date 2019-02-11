package com.mantzavelas.tripassistantapi.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

	private static Properties applicationProperties;
	private static void initialize() {
		if (applicationProperties == null) {
			applicationProperties = new Properties();
			try {
				applicationProperties.load(new FileInputStream("src/main/resources/application.properties"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getProperty(String property) {
		initialize();
		return applicationProperties.getProperty(property);
	}

}
