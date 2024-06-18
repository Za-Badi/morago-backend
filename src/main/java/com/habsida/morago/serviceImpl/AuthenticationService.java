package com.habsida.morago.serviceImpl;

import com.habsida.morago.exceptions.GlobalException;
import com.habsida.morago.model.entity.*;
import com.habsida.morago.model.inputs.LoginUserInput;
import com.habsida.morago.model.inputs.RegisterUserInput;
import com.habsida.morago.model.inputs.UserInput;
import com.habsida.morago.repository.FileRepository;
import com.habsida.morago.repository.RoleRepository;
import com.habsida.morago.repository.UserRepository;
import graphql.GraphQLException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public User signUpAsUser(RegisterUserInput registerUserInput) throws GlobalException {
        if (registerUserInput.getPhone() == null || registerUserInput.getPhone().isBlank() ||
                registerUserInput.getPassword() == null || registerUserInput.getPassword().isBlank()) {
            throw new GlobalException("Empty values are not allowed");
        }
        String phoneInput = registerUserInput.getPhone().replaceAll("\\D", "");
        if (phoneInput.isBlank()) {
            throw new GlobalException("Phone number must contain at least one digit");
        }
        if (userRepository.findByPhone(registerUserInput.getPhone()).isPresent()) {
            throw new GlobalException("Phone number is already used: " + registerUserInput.getPhone());
        }
        User user = new User();
        user.setPhone(phoneInput);
        user.setPassword(passwordEncoder.encode(registerUserInput.getPassword()));
        user.setImage(null);
        user.setTranslatorProfile(null);
        List<Role> roles = new ArrayList<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });
        roles.add(userRole);
        user.setRoles(roles);
        user.setUserProfile(new UserProfile());
        return userRepository.save(user);
    }

    public User signUpAsTranslator(RegisterUserInput registerUserInput) throws GlobalException {
        if (registerUserInput.getPhone() == null || registerUserInput.getPhone().isBlank() ||
                registerUserInput.getPassword() == null || registerUserInput.getPassword().isBlank()) {
            throw new GlobalException("Empty values are not allowed");
        }
        String phoneInput = registerUserInput.getPhone().replaceAll("\\D", "");
        if (phoneInput.isBlank()) {
            throw new GlobalException("Phone number must contain at least one digit");
        }
        if (userRepository.findByPhone(registerUserInput.getPhone()).isPresent()) {
            throw new GlobalException("Phone number is already used: " + registerUserInput.getPhone());
        }
        User user = new User();
        user.setPhone(phoneInput);
        user.setPhone(registerUserInput.getPhone());
        user.setPassword(passwordEncoder.encode(registerUserInput.getPassword()));
        user.setImage(null);
        user.setUserProfile(null);
        List<Role> roles = new ArrayList<>();
        Role translatorRole = roleRepository.findByName("ROLE_TRANSLATOR")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_TRANSLATOR");
                    return roleRepository.save(newRole);
                });
        roles.add(translatorRole);
        user.setRoles(roles);
        List<Language> languages = new ArrayList<>();
        List<Theme> themes = new ArrayList<>();
        TranslatorProfile translatorProfile = new TranslatorProfile();
        translatorProfile.setLanguages(languages);
        translatorProfile.setThemes(themes);
        user.setTranslatorProfile(translatorProfile);
        return userRepository.save(user);
    }

    public User logIn(LoginUserInput loginUserInput) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserInput.getPhone(),
                        loginUserInput.getPassword()
                )
        );
        return userRepository.findByPhone(loginUserInput.getPhone())
                .orElseThrow();
    }
}