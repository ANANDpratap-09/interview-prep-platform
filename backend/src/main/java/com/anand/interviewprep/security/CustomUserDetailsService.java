package com.anand.interviewprep.security;

import com.anand.interviewprep.entity.User;
import com.anand.interviewprep.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Spring Security doesn't know about our User entity — it works with its
 * own UserDetails interface. This class bridges the two: given an email,
 * it loads our User from the DB and wraps it into something Security
 * understands, with the role turned into a "ROLE_X" authority.
 */
@Service
@RequiredArgsConstructor // Lombok: generates a constructor for final fields (dependency injection)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}