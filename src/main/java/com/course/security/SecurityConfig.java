package com.course.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity   // enables @PreAuthorize
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ===============================
    // PASSWORD ENCODER (BCrypt)
    // ===============================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===============================
    // AUTHENTICATION MANAGER
    // ===============================
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ===============================
    // SECURITY FILTER CHAIN
    // ===============================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF (JWT based)
            .csrf(csrf -> csrf.disable())
            
            // ✅ ENABLE CORS (IMPORTANT)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))


            // Stateless session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

            	    // PUBLIC
            	    .requestMatchers("/api/auth/**").permitAll()

            	    // ADMIN
            	    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

            	    // INSTRUCTOR
            	    .requestMatchers("/api/instructor/**").hasAuthority("ROLE_INSTRUCTOR")
            	    .requestMatchers(HttpMethod.POST, "/api/courses").hasAuthority("ROLE_INSTRUCTOR")
            	    .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAuthority("ROLE_INSTRUCTOR")
            	    .requestMatchers("/api/courses/my").hasAuthority("ROLE_INSTRUCTOR")

            	    // STUDENT
            	    .requestMatchers("/api/enrollments/**")
            	        .hasAuthority("ROLE_STUDENT")

            	    // SHARED
            	    .requestMatchers(HttpMethod.GET, "/api/courses/**")
            	        .hasAnyAuthority("ROLE_ADMIN", "ROLE_INSTRUCTOR", "ROLE_STUDENT")

            	    .anyRequest().authenticated()
            	)



            // Add JWT filter
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
    
    
 // ===============================
 // CORS CONFIGURATION (SECURITY)
 // ===============================
 @Bean
 public CorsConfigurationSource corsConfigurationSource() {

     CorsConfiguration config = new CorsConfiguration();
     config.setAllowedOrigins(List.of("http://localhost:3000","https://studentcoursemanagement-frontend.vercel.app"));
     config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
     config.setAllowedHeaders(List.of("*"));
     config.setAllowCredentials(true);

     UrlBasedCorsConfigurationSource source =
             new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", config);

     return source;
 }

}

