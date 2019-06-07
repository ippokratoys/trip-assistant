package com.mantzavelas.tripassistantapi.controllers;

import com.mantzavelas.tripassistantapi.annotations.AuthenticatedUser;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.repositories.UserRepository;
import com.mantzavelas.tripassistantapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;

	@Autowired
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@PostMapping("/deviceToken")
	public ResponseEntity addDeviceToken(@AuthenticatedUser User user, @RequestBody String token) {
		userService.registerDeviceToken(user, token);

		return ResponseEntity.ok().build();
	}
}
