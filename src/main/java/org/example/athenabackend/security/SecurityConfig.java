package org.example.athenabackend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final HttpServletRequest httpServletRequest;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf( c -> c.disable() );
        http.headers(headers -> headers.frameOptions(
              frameOptions ->  frameOptions.disable()
        ));
        http.cors(c -> {
            CorsConfigurationSource source = new CorsConfigurationSource(){
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                   CorsConfiguration config = new CorsConfiguration();
                   config.setAllowCredentials(true);
                   config.setAllowedMethods(List.of("*"));
                   config.setAllowedOrigins(List.of("http://localhost:5173"));
                   config.setAllowedHeaders(List.of("*"));
                   config.setExposedHeaders(List.of("*"));
                    return config;
                }
            };
            c.configurationSource(source);
        });
        http.authorizeHttpRequests(r -> {
            r.requestMatchers("/","/api/auth/**",
                    "/api/students/**",
                    "api/teachers/**"
                    , "/api/parents/**",
                    "api/files/**",
                    "api/classrooms/**",
                    "api/exams/**",
                    "api/fees/**",
                    "api/attendances/**",
                    "api/subjects/**"
                    ).permitAll();
            r.anyRequest().authenticated();
        }).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
