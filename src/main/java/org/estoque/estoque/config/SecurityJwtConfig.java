package org.estoque.estoque.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.estoque.estoque.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
@Profile("jwt")
public class SecurityJwtConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityJwtConfig(
            JwtAuthFilter jwtAuthFilter,
            AccessDeniedHandler customAccessDeniedHandler,
            AuthenticationEntryPoint customAuthenticationEntryPoint)
    {
        this.jwtAuthFilter = jwtAuthFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
            // disable csrf validation
            .csrf(AbstractHttpConfigurer::disable)
            // authorized and unauthorized requests
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/token").permitAll()
                .anyRequest().authenticated()
            )
            // disable http basic
            .httpBasic(AbstractHttpConfigurer::disable)
            // add filter jwt before
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            // handler exceptions
            .exceptionHandling((exception) -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            );
        return http.build();
    }
}
