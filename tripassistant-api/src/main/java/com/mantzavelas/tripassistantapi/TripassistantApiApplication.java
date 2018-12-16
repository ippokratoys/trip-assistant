package com.mantzavelas.tripassistantapi;

import com.mantzavelas.tripassistantapi.services.PhotoServiceThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.mantzavelas.tripassistantapi.repositories")
public class TripassistantApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripassistantApiApplication.class, args);

		Thread photoServiceThread = new Thread(new PhotoServiceThread());
		photoServiceThread.start();
	}
}
