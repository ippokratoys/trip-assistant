package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.converters.UserResourceToUserConverter;
import com.mantzavelas.tripassistantapi.exceptions.UsernameAlreadyInUseException;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.models.UserCredentials;
import com.mantzavelas.tripassistantapi.repositories.UserRepository;
import com.mantzavelas.tripassistantapi.resources.UserResource;
import com.mantzavelas.tripassistantapi.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    private final UserResourceToUserConverter converter;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserResourceToUserConverter converter) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.converter = converter;
    }

    public String signUp(UserResource userResource) {
        if (userRepository.existsByUsername(userResource.getUsername())) {
            throw new UsernameAlreadyInUseException(String.format("Username %s is already in use.", userResource.getUsername()));
        }

        String rawPassword = userResource.getPassword();
        User newUser = converter.convert(userResource);
        userRepository.save(newUser);

        //After successfull signup, sign in the user
        UserCredentials userCredentials = new UserCredentials(userResource.getUsername(), rawPassword);
        return signIn(userCredentials);
    }

    public String signIn(UserCredentials userCredentials) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userCredentials.getUsername(),
                        userCredentials.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);
    }

}
