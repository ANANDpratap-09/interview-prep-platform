package com.anand.interviewprep.controller;

import com.anand.interviewprep.dto.UserResponse;
import com.anand.interviewprep.entity.User;
import com.anand.interviewprep.exception.ApiException;
import com.anand.interviewprep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints for the logged-in user's own profile.
 * The `Authentication` parameter is filled in automatically by Spring Security
 * using whatever JwtAuthFilter put into the SecurityContext — its getName()
 * returns the email, since that's what we set as the token's subject.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        User user = findByEmail(authentication.getName());
        return toResponse(user);
    }

    @PutMapping("/me")
    public UserResponse updateName(Authentication authentication, @RequestBody UpdateNameRequest request) {
        User user = findByEmail(authentication.getName());
        user.setName(request.getName());
        userRepository.save(user);
        return toResponse(user);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(),
                user.getRole().name(), user.getCreatedAt());
    }

    /** Tiny inline DTO — just one field, not worth its own file. */
    public static class UpdateNameRequest {
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}