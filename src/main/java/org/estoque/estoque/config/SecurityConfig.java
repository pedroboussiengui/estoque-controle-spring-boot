//package org.estoque.estoque.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorize) -> authorize
////                .requestMatchers("/api/v1/token").permitAll()
//                .anyRequest().authenticated()
//        );
//        return http.build();
//    }
//}
