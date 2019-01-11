package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.exceptions.BadRequestException;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.models.UserCredentials;
import com.mantzavelas.tripassistantapi.repositories.UserRepository;
import com.mantzavelas.tripassistantapi.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public String signUp(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException(String.format("Username %s is already in use.", user.getUsername()));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        //After successfull signup, sign in the user
        UserCredentials userCredentials = new UserCredentials(user.getUsername(), user.getPassword());
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
