package com.backend.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.backend.entities.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Custom UserDetails implementation used by Spring Security.
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final User user;

    /**
     * Returns user roles as Spring Security authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()));
    }

    /**
     * Returns encrypted password.
     */
    @Override
    public String getPassword() {

        return user.getPassword();
    }

    /**
     * Returns username used for authentication.
     * Email is used as username.
     */
    @Override
    public String getUsername() {

        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    /*
     * Application-specific helper methods.
     */

    public Long getUserId() {

        return user.getId();
    }

    public String getFullName() {

        return user.getFullName();
    }

    public String getRole() {

        return user.getRole().name();
    }

    public User getUser() {

        return user;
    }
}