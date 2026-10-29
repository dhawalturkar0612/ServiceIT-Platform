package com.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.entities.User;
import com.backend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of Spring Security UserDetailsService.
 *
 * Responsible for loading user details from the database
 * during authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user by email.
     *
     * Spring Security automatically calls this method
     * during authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email : " + email));

        return new CustomUserDetails(user);
    }
}