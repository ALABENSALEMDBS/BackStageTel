package com.example.backstagetel.Configurations;


import com.example.backstagetel.Services.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthorizationFilter  extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UtilisateurService utilisateurService;

    public JWTAuthorizationFilter(JwtUtils jwtUtils, UtilisateurService utilisateurService) {
        this.jwtUtils = jwtUtils;
        this.utilisateurService = utilisateurService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String emailuser = jwtUtils.extractUsername(token);

        if (emailuser != null && SecurityContextHolder.getContext().getAuthentication() ==
                null) {
            UserDetails userDetails = utilisateurService.loadUserByUsername(emailuser);

            if (jwtUtils.validateToken(token, userDetails)) {
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Authenticated user: " + emailuser + " with role: " + userDetails.getAuthorities());
            } else {
                System.out.println("Token validation failed for user: " + emailuser);
            }
        } else {
            System.out.println("Username is null or context already has authentication.");
        }

        filterChain.doFilter(request, response);
    }
}
