package com.example.backstagetel.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(@Lazy JWTAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/auth/login",
                                "/user/auth/register",
                                "/user/check-email-exists",
                                "/user/auth/logout",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/user/forgot-password",
                                "user/reset-password",
                                "user/upload"
                        ).permitAll()
                          .requestMatchers("/user/admin-only").hasAuthority("ROLE_ADMIN")
                          .requestMatchers(HttpMethod.GET, "/user/admin-only").hasAuthority("ROLE_ADMIN")
                          .requestMatchers("/user/change-password").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENT", "ROLE_AGENT")
                          .requestMatchers("/user/getAllUsers").hasAuthority("ROLE_CLIENT")
                          .requestMatchers( "/user/update-photo/{id}").hasAnyAuthority("ROLE_CLIENT", "ROLE_AGENT", "ROLE_ADMIN")
                          .requestMatchers( "/user/clients").hasAnyAuthority( "ROLE_AGENT", "ROLE_ADMIN")
                          .requestMatchers(HttpMethod.PUT,"/user/toggle-statut/{id}").hasAnyAuthority("ROLE_ADMIN")
                          .requestMatchers("/user/creercomptebyadmin").hasAuthority("ROLE_ADMIN")
                          .requestMatchers("/user/agents").hasAuthority("ROLE_ADMIN")
                          .requestMatchers("/user/delete/{id}").hasAuthority("ROLE_ADMIN")

                          .requestMatchers("/reclamation/ajouter/{idUser}").hasAuthority("ROLE_CLIENT")
                          .requestMatchers("/reclamation/getReclByUser/{idUser}").hasAnyAuthority("ROLE_CLIENT", "ROLE_AGENT", "ROLE_ADMIN")
                          .requestMatchers("/reclamation/delete/{id}").hasAnyAuthority( "ROLE_AGENT", "ROLE_ADMIN","ROLE_CLIENT")
                          .requestMatchers(HttpMethod.GET, "/reclamation/getReclamations", "/reclamation/reclamationBYId/{idReclamation}").hasAnyAuthority( "ROLE_AGENT", "ROLE_ADMIN")
                          .requestMatchers("/reclamation/modifierRecl/{id}").hasAnyAuthority( "ROLE_AGENT", "ROLE_ADMIN","ROLE_CLIENT")
                          .requestMatchers("/reclamation/en-cours/{idReclamation}").hasAuthority( "ROLE_AGENT")
                          .requestMatchers("/reclamation/rejetee/{idReclamation}").hasAuthority( "ROLE_AGENT")
                          .requestMatchers("/reclamation/repondre/{idReclamation}").hasAuthority( "ROLE_AGENT")
                          .requestMatchers("/reclamation/by-user/{email}/{numeroConcerne}").hasAuthority( "ROLE_AGENT")


                          .requestMatchers("/renseignement/ajouter/{idUser}").hasAuthority( "ROLE_CLIENT")
                          .requestMatchers("/renseignement/getbyuser/{idUser}").hasAuthority( "ROLE_CLIENT")
                          .requestMatchers("/renseignement/listeRens").hasAnyAuthority( "ROLE_ADMIN", "ROLE_AGENT")
                         .requestMatchers("/renseignement/repondre/{idRenseignement}").hasAuthority( "ROLE_AGENT")

                         .requestMatchers("/avis/add/{idReclamation}").hasAuthority( "ROLE_CLIENT")
                         .requestMatchers("/avis/all").hasAnyAuthority( "ROLE_ADMIN", "ROLE_AGENT")



//
                          .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
