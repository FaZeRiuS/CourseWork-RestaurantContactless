package com.example.CourseWork.addition;

import com.example.CourseWork.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**", "/register", "/home","/login.html", "/registration.html","/error.html","/oauth2/**").permitAll()
                        .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**").hasRole(Role.ADMINISTRATOR.name())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")  // instead of 'username'
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login.html")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/home.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login.html?logout=true")
                        .permitAll()
                );
        return http.build();
    }

//    private List<GrantedAuthority> determineAuthorities(String email) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if ("admin@example.com".equalsIgnoreCase(email)) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
//        } else if ("chef@example.com".equalsIgnoreCase(email)) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_CHEF"));
//        } else {
//            authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
//        }
//
//        return authorities;
//    }

}
