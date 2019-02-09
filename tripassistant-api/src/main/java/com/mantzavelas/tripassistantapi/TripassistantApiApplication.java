package com.mantzavelas.tripassistantapi;

import com.mantzavelas.tripassistantapi.services.CategoryTaggingThread;
import com.mantzavelas.tripassistantapi.services.PhotoServiceThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.mantzavelas.tripassistantapi.repositories")
public class TripassistantApiApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TripassistantApiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(TripassistantApiApplication.class, args);

		Thread photoServiceThread = new Thread(new PhotoServiceThread());
		photoServiceThread.start();
		Thread categoryTagging = new Thread(new CategoryTaggingThread());
		categoryTagging.start();
	}
}
