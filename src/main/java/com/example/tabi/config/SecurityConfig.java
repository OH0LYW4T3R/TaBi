package com.example.tabi.config;

import com.example.tabi.login.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenService refreshTokenService, CustomUserDetailsService customUserDetailsService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/api/app-user/sign-up/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs").permitAll()
                        .requestMatchers("/error").permitAll()
                        //.requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/api/auth/login", "api/auth/refresh").permitAll()
                        .anyRequest().authenticated()

        ).addFilter(new UsernamePasswordJwtLoginFilter(authenticationManager, jwtUtil, refreshTokenService))
                .addFilterBefore(new JwtVerificationFilter(jwtUtil, customUserDetailsService), UsernamePasswordJwtLoginFilter.class);
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}