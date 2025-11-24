package com.reffocase.backend.trainerapp.backend_trainerapp.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.filters.JwtAuthenticationFilter;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.filters.JwtValidationFilter;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.UserRepository;

@Configuration
public class SpringSecurityConfig {


    private final UserRepository userRepository;

    public SpringSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authRules -> authRules
                // Trainers
                .requestMatchers(HttpMethod.GET, "/api/formadores").hasAuthority("KEY_READ_TRAINERS")
                .requestMatchers(HttpMethod.GET, "/api/formadores/{id}").hasAuthority("KEY_READ_TRAINERS")
                .requestMatchers(HttpMethod.POST, "/api/formadores").hasAuthority("KEY_WRITE_TRAINERS")
                .requestMatchers(HttpMethod.PUT, "/api/formadores/{id}").hasAuthority("KEY_WRITE_TRAINERS")
                .requestMatchers(HttpMethod.DELETE, "/api/formadores/{id}").hasAuthority("KEY_DELETE_TRAINERS")
                .requestMatchers(HttpMethod.GET, "/api/formadores/list").hasAuthority("KEY_READ_METRICS")
                
                // Thematics
                .requestMatchers(HttpMethod.GET, "/api/tematicas/").hasAuthority("KEY_READ_THEMATICS")
                .requestMatchers(HttpMethod.GET, "/api/tematicas").hasAuthority("KEY_READ_THEMATICS")
                .requestMatchers(HttpMethod.GET, "/api/tematicas/{id}").hasAuthority("KEY_READ_THEMATICS")
                .requestMatchers(HttpMethod.POST, "/api/tematicas").hasAuthority("KEY_WRITE_THEMATICS")
                .requestMatchers(HttpMethod.PUT, "/api/tematicas/{id}").hasAuthority("KEY_WRITE_THEMATICS")
                .requestMatchers(HttpMethod.DELETE, "/api/tematicas/{id}").hasAuthority("KEY_DELETE_THEMATICS")
                // Document Type
                .requestMatchers(HttpMethod.GET, "/api/tipo_documento").permitAll()
                // Provinces
                .requestMatchers(HttpMethod.GET, "/api/provincias").permitAll()
                // Roles
                .requestMatchers(HttpMethod.GET, "/api/roles").permitAll()

                // Trainings
                .requestMatchers(HttpMethod.GET, "/api/capacitaciones").hasAuthority("KEY_READ_TRAININGS")
                .requestMatchers(HttpMethod.GET, "/api/capacitaciones/{id}").hasAuthority("KEY_READ_TRAININGS")
                .requestMatchers(HttpMethod.POST, "/api/capacitaciones").hasAuthority("KEY_WRITE_TRAININGS")
                .requestMatchers(HttpMethod.PUT, "/api/capacitaciones/{id}").hasAuthority("KEY_WRITE_TRAININGS")
                .requestMatchers(HttpMethod.DELETE, "/api/capacitaciones/{id}").hasAuthority("KEY_DELETE_TRAININGS")
                .requestMatchers(HttpMethod.GET, "/api/capacitaciones-list").hasAuthority("KEY_READ_TRAININGS")

                // Users
                .requestMatchers(HttpMethod.GET, "/api/users/").hasAuthority("KEY_READ_USERS")
                .requestMatchers(HttpMethod.PUT, "/api/users/*").hasAuthority("KEY_WRITE_USERS")
                .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("KEY_WRITE_USERS")

                // Loggin User
                .requestMatchers(HttpMethod.PUT, "/api/perfil").permitAll()

                // Validacion

                .requestMatchers(HttpMethod.PUT, "/api/validacion").permitAll()

                .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(),
                        userRepository))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
		"http:/redfocase.com.ar",
        "http:/www.redfocase.com.ar",
		"http://localhost:5173"));
	config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        @SuppressWarnings("null")
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
