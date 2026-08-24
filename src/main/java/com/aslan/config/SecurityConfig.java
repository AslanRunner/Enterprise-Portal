package com.aslan.config;

import com.aslan.jwt.AuthEntryPoint;
import com.aslan.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        private final AuthenticationProvider authenticationProvider;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthEntryPoint authEntryPoint;

        public static final String[] SWAGGER_PATHS = {
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
        };

        public SecurityConfig(
                        AuthenticationProvider authenticationProvider,
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthEntryPoint authEntryPoint) {
                this.authenticationProvider = authenticationProvider;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.authEntryPoint = authEntryPoint;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(request -> request
                                                .requestMatchers("/", "/index.html", "/favicon.ico", "/assets/**")
                                                .permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers(SWAGGER_PATHS).permitAll()
                                                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                                                .anyRequest().authenticated())
                                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

                return http.build();
        }
}
