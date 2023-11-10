package com.cbo.CBO_NFOS_ICMS.services;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.User;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.UserRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inject the UserRepository bean
    @Autowired
    private UserRepository userRepository;

    // Override the loadUserByUsername method and return a UserDetailsImpl object
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Find the user by username from the userRepository
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Get the roles of the user and map them to GrantedAuthority objects
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        // Create and return a UserDetailsImpl object with the user's username, password, and authorities
        return new UserDetailsImpl(user.getUsername(), user.getPassword(), authorities);
    }
}

