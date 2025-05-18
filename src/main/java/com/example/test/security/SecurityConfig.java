package com.example.test.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter)
    {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    // ======== PRODUCTS ========
                    .requestMatchers(HttpMethod.GET, "/products/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                    // ======== USERS ========
                    .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                    // ======== ORDERS ========
                    .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/orders/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/orders/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/orders/**").hasRole("ADMIN")
                    // ======== ORDER-ITEMS ========
                    .requestMatchers(HttpMethod.GET, "/order-items/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/order-items/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/order-items/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/order-items/**").hasRole("ADMIN")
                    // ======== CART-ITEMS ========
                    .requestMatchers(HttpMethod.GET, "/cart-items/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/cart-items/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/cart-items/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/cart-items/**").hasAnyRole("USER", "ADMIN")

                    .anyRequest().authenticated()

            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
