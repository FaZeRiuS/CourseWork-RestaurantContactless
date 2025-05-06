package com.example.CourseWork.service;

import com.example.CourseWork.dto.UserDto;
import com.example.CourseWork.dto.UserRegistrationDto;

import java.util.Optional;

public interface UserService {
    UserDto register(UserRegistrationDto dto);
    Optional<UserDto> authenticate(String email, String password);
    Optional<UserDto> getUserById(Integer id);
}
