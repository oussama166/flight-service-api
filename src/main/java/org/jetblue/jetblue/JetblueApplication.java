package org.jetblue.jetblue;

import io.github.cdimascio.dotenv.Dotenv;
import org.jetblue.jetblue.Config.RSAkeyProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableConfigurationProperties(RSAkeyProps.class)
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class JetblueApplication {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
        } catch (Exception e) {
            System.err.println("Could not load .env file: " + e.getMessage());
        }

        SpringApplication.run(JetblueApplication.class, args);
    }
}
