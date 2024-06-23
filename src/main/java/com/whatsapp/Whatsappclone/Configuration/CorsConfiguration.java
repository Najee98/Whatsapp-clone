package com.whatsapp.Whatsappclone.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class sets up the Cross-Origin Resource Sharing (CORS) configuration.
 * CORS is a security feature implemented by web browsers to control how resources
 * on a web page can be requested from another domain outside the domain from which
 * the resource originated.
 */
@Configuration // Indicates that this class is a configuration class for Spring
public class CorsConfiguration implements WebMvcConfigurer {

    /**
     * Configures CORS mapping for the entire application.
     *
     * @param registry The CorsRegistry object used to define CORS settings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all paths in the application
                .allowedOrigins("http://localhost:5173", "http://localhost:5174", "http://localhost:5175")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow these HTTP methods
                .allowedHeaders("*") // Allow all headers in requests
                .exposedHeaders("Authorization"); // Expose the "Authorization" header to clients
    }
}
