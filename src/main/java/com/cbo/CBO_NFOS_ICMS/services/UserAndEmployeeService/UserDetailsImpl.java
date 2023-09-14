package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    // Declare the fields for username, password, and authorities
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // Create a constructor with the fields as parameters
    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // Override the getUsername method and return the username field
    @Override
    public String getUsername() {
        return username;
    }

    // Override the getPassword method and return the password field
    @Override
    public String getPassword() {
        return password;
    }

    // Override the getAuthorities method and return the authorities field
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Override the isAccountNonExpired method and return true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Override the isAccountNonLocked method and return true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Override the isCredentialsNonExpired method and return true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Override the isEnabled method and return true
    @Override
    public boolean isEnabled() {
        return true;
    }
}

