package com.mantzavelas.tripassistantapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertyLoader {

	private static Environment environment;

	@Autowired
	public PropertyLoader(Environment environment) { PropertyLoader.environment = environment;}

	public String getProperty(String property) {
		return environment.getProperty(property);
	}

}
