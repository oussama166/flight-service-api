package org.jetblue.jetblue;

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
        SpringApplication.run(JetblueApplication.class, args);
    }
}
