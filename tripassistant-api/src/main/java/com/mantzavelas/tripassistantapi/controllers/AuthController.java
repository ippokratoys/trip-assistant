package com.mantzavelas.tripassistantapi.controllers;

import com.mantzavelas.tripassistantapi.dtos.AuthTokenDto;
import com.mantzavelas.tripassistantapi.models.UserCredentials;
import com.mantzavelas.tripassistantapi.resources.UserResource;
import com.mantzavelas.tripassistantapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public AuthTokenDto signUp(@RequestBody UserResource resource) {
        return new AuthTokenDto(userService.signUp(resource));
    }

    @PostMapping("/signin")
    public AuthTokenDto signIn(@RequestBody UserCredentials userCredentials) {
        return new AuthTokenDto(userService.signIn(userCredentials));
    }
}
