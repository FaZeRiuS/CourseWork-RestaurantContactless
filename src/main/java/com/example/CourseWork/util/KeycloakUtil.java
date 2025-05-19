package com.example.CourseWork.util;

import com.example.CourseWork.model.KeycloakUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakUtil {
    
    public static KeycloakUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            
            KeycloakUser user = new KeycloakUser();
            user.setId(jwt.getSubject());
            user.setUsername(jwt.getClaimAsString("preferred_username"));
            user.setEmail(jwt.getClaimAsString("email"));
            user.setFirstName(jwt.getClaimAsString("given_name"));
            user.setLastName(jwt.getClaimAsString("family_name"));
            
            return user;
        }
        throw new RuntimeException("User not authenticated");
    }
} 