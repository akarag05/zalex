package com.zalex.employee_certification.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class EnvConfig {

    private final ConfigurableEnvironment environment;

    public EnvConfig(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void loadEnvFile() {
        String activeProfile = environment.getActiveProfiles().length > 0 
            ? environment.getActiveProfiles()[0] 
            : "dev";
        
        String envFileName = ".env." + activeProfile;
        
        try {
            Dotenv dotenv = Dotenv.configure()
                .filename(envFileName)
                .ignoreIfMissing()
                .load();
            
            // Load variables from .env file into system properties for Spring Boot
            String dbUrl = dotenv.get("DB_URL");
            String dbUsername = dotenv.get("DB_USERNAME");
            String dbPassword = dotenv.get("DB_PASSWORD");
            
            if (dbUrl != null) {
                System.setProperty("DB_URL", dbUrl);
            }
            if (dbUsername != null) {
                System.setProperty("DB_USERNAME", dbUsername);
            }
            if (dbPassword != null) {
                System.setProperty("DB_PASSWORD", dbPassword);
            }
        } catch (Exception e) {
            // If .env file doesn't exist, continue with default configuration
            System.out.println("Note: " + envFileName + " not found. Using default configuration.");
        }
    }
}

