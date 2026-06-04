package org.example.cargallery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/h2-console/**", "/", "/index.html", "/app.js", "/css/**", "/js/**", "/logos/**", "/error").permitAll()

                        .requestMatchers(HttpMethod.GET, "/cars/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/cars/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/cars/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/customers/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/customers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/customers/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/sales/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/sales/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/sales/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/sales/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults())

                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}