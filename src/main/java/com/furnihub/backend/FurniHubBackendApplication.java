package com.furnihub.backend;

import com.furnihub.backend.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class FurniHubBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FurniHubBackendApplication.class, args);
    }
}
