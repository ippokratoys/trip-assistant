package com.mantzavelas.tripassistantapi.converters;

import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.resources.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserResourceToUserConverter implements Converter<UserResource, User> {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserResourceToUserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User convert(UserResource source) {
        User user = new User();

        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setUsername(source.getUsername());
        user.setPassword(passwordEncoder.encode(source.getPassword()));

        return user;
    }
}
