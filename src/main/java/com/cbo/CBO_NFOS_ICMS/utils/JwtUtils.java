package com.cbo.CBO_NFOS_ICMS.utils;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    // Inject the secret key and expiration time from application.properties
    @Value("${application.security.jwt.secret-key}")
    private String secret;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    // Create a JWT token from a username, date, and authorities
    public String createToken(String username, Date date, Collection<? extends GrantedAuthority> authorities) {
        // Get the current date
        Date now = new Date();

        // Calculate the expiration date
        Date expiryDate = new Date(now.getTime() + expiration);

        // Create a JWT builder
        JwtBuilder builder = Jwts.builder()
                .setSubject(username) // Set the username as the subject
                .setIssuedAt(now) // Set the issued date
                .setExpiration(expiryDate) // Set the expiration date
                .signWith(SignatureAlgorithm.HS512, secret); // Sign with the secret key and algorithm

        // Add the authorities as a claim
        if (authorities != null && !authorities.isEmpty()) {
            builder.claim("authorities", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        }

        // Build and return the token
        return builder.compact();
    }

    // Validate a JWT token and return true if it is valid, false otherwise
    public boolean validateToken(String token) {
        try {
            // Parse the token and check if it is expired or malformed
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            // Log the exception and return false
            return false;
        }
    }

    // Get the username from a JWT token
    public String getUsernameFromToken(String token) {
        // Parse the token and get the subject (username)
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
