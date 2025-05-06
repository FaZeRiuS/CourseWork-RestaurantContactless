package com.example.CourseWork.addition;

import com.example.CourseWork.model.User;
import com.example.CourseWork.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.CourseWork.addition.Role.*;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner boot(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setName("Admin");
                admin.setPasswordHash(passwordEncoder.encode("adminpassword"));
                admin.setRole(ADMINISTRATOR);
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("user@example.com").isEmpty()) {
                User user = new User();
                user.setEmail("user@example.com");
                user.setName("User");
                user.setPasswordHash(passwordEncoder.encode("userpassword"));
                user.setRole(CUSTOMER);
                userRepository.save(user);
            }
        };
    }
}
