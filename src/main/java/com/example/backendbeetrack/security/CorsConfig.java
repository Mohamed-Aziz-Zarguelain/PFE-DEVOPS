package com.example.backendbeetrack.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Applique CORS à toutes les routes
                        .allowedOrigins("*") // Autorise toutes les origines
                        .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS") // Autorise toutes les méthodes
                        .allowedHeaders("*") // Autorise tous les headers
                        .allowCredentials(false); // mettre true si besoin de cookies/session
            }
        };
    }
}
