package com.cbo.CBO_NFOS_ICMS.services;



import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.User;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.UserRepository;
import com.cbo.CBO_NFOS_ICMS.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtils jwtUtil;


    @Autowired
    private AuthenticationManager authenticationManager;

//    @Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepo.findByUserName(username);

        if (user != null ){

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
        }
        else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }


    private Set getAuthorities(User user){
        Set authorities = new HashSet();
        if (user != null){
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName().toString()));
            });
        }

        return authorities;
    }


//    private void authenticate(String username, String password) throws Exception {
//        try {
//            User user = userRepo.findByUserName(username);
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password, getAuthorities(user)));
//
//        } catch (DisabledException e){
//            throw new IncorrectUsernameOrPasswordException("User is disabled");
//        } catch ( BadCredentialsException e){
//            throw new IncorrectUsernameOrPasswordException("Incorrect Username Or Password!");
//        } catch (AuthenticationException e){
//            throw new IncorrectUsernameOrPasswordException("Authentication is failed");
//        }
//    }


}
