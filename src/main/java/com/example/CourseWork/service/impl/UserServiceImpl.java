package com.example.CourseWork.service.impl;

import com.example.CourseWork.addition.Role;
import com.example.CourseWork.dto.UserDto;
import com.example.CourseWork.dto.UserRegistrationDto;
import com.example.CourseWork.model.User;
import com.example.CourseWork.repository.UserRepository;
import com.example.CourseWork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegistrationDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.CUSTOMER);

        User saved = userRepository.save(user);

        return new UserDto(saved.getId(), saved.getName(), saved.getEmail(), user.getPasswordHash(), saved.getRole());
    }


    @Override
    public Optional<UserDto> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))  // Match password
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPasswordHash(), user.getRole()));  // Convert to UserDto
    }

    @Override
    public Optional<UserDto> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPasswordHash(), user.getRole()));  // Convert User to UserDto
    }

}
