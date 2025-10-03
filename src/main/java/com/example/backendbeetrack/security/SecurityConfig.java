package com.example.backendbeetrack.security;

import com.example.backendbeetrack.repositories.UserRepository;
import com.example.backendbeetrack.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // ✅ Disable CSRF (required for stateless APIs)
                .cors(cors -> cors.configure(http)) // Optional: enable CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()  // ✅ Allow public access
                        .requestMatchers("/h2-console/**").permitAll() // Optional: if using H2
                        .requestMatchers(HttpMethod.GET, "/ruchers/**").permitAll()
                        .requestMatchers("/actuator/**", "/prometheus/**").permitAll()
                        .requestMatchers("/prometheus").permitAll() 
                         // Only allow /prometheus endpoint
                        .requestMatchers(HttpMethod.POST, "/ruchers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/recoltes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/recoltes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/ruchers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/**").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/ruchers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ruches/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ruches/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/ruches/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/ruches/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/recoltes/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/recoltes/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/recoltes/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/recoltes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/interventions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/etat-ruches/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/interventions/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/interventions/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/interventions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/taches/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/taches/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/taches/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/taches/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/depenses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/depenses/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/depenses/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/depenses/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/revenus/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/revenus/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/revenus/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/revenus/**").permitAll()

                        .anyRequest().authenticated()
                );



            return http.build();

    }


}