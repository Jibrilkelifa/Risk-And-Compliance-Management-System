package com.cbo.CBO_NFOS_ICMS.configuration;

import com.cbo.CBO_NFOS_ICMS.services.UserDetailsServiceImpl;
import com.cbo.CBO_NFOS_ICMS.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Inject the JwtUtils and UserDetailsServiceImpl beans
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the authorization header from the request
        String header = request.getHeader("Authorization");

        // Check if the header is valid and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // Get the token from the header
            String token = header.substring(7);

            // Validate the token
            if (jwtUtils.validateToken(token)) {
                // Get the username from the token
                String username = jwtUtils.getUsernameFromToken(token);
                List<GrantedAuthority> roles = jwtUtils.grantedAuthorities(token);
                // Create an authentication object from the user details and token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token, null, roles);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication to the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

}


