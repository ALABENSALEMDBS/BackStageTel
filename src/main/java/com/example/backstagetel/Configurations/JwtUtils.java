package com.example.backstagetel.Configurations;

import com.example.backstagetel.Entities.Role;
import com.example.backstagetel.Entities.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtUtils {
    //private static final String SECRET = "ZrbA0vVddNSLK0rk3L//Cpch/TBvpYiUx4NY126peoc=";
    private static final String SECRET = "CaG+2PqGn03vZb5GzeGSYGvveTPo1mK2Lf5RHCwL6mk=";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));


    /**
     * Génère un token JWT à partir de l'identifiant et des rôles.
     */
    public String generateToken(Utilisateur user) {
        return generateToken(user.getUsername(), user.getRole());
    }


    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.getNomRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }



    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
