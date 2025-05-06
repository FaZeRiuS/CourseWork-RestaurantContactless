package com.example.CourseWork;

import com.example.CourseWork.dto.UserDto;
import com.example.CourseWork.dto.UserRegistrationDto;
import com.example.CourseWork.model.User;
import com.example.CourseWork.repository.UserRepository;
import com.example.CourseWork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.CourseWork.addition.Role.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldReturnUserDto() {
        UserRegistrationDto registerDto = new UserRegistrationDto();
        registerDto.setName("John");
        registerDto.setEmail("john@example.com");
        registerDto.setPassword("secret");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setName("John");
        savedUser.setEmail("john@example.com");
        savedUser.setPasswordHash("hashed");
        savedUser.setRole(CUSTOMER);

        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.register(registerDto);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(1, result.getId());
    }
}
