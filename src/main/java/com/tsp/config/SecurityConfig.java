package com.tsp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, LogRequestParamsFilter logRequestParamsFilter, OAuth2SuccessHandler oauth2SuccessHandler) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register", "/oauth2/**").permitAll()  // Allow login, register, and OAuth2 URLs
                .requestMatchers("/tsp/solve").authenticated()  // Protect /tsp/solve endpoint (authentication required)
                .anyRequest().authenticated()  // Require authentication for other requests
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")  // Use 'email' instead of 'username'
                .passwordParameter("password")
                .defaultSuccessUrl("/welcome", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/welcome", true)
                .failureUrl("/login?error=true")
                .successHandler(oauth2SuccessHandler)
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());  // Disable CSRF for testing (can be enabled in production)

        // Add custom filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(logRequestParamsFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Custom filter to log request parameters (for debugging).
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    LogRequestParamsFilter logRequestParamsFilter() {
        return new LogRequestParamsFilter();
    }

    // Custom filter to log request parameters for debugging purposes
    public static class LogRequestParamsFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if ("POST".equalsIgnoreCase(request.getMethod()) && request.getRequestURI().equals("/login")) {
                System.out.println("Request URL: " + request.getRequestURL());
                System.out.println("Request Method: " + request.getMethod());

                // Log all request parameters
                request.getParameterMap().forEach((key, value) -> {
                    System.out.println("Parameter: " + key + " = " + String.join(", ", value));
                });
            }
            filterChain.doFilter(request, response);
        }
    }
}
