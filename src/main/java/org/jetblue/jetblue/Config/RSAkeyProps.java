package org.jetblue.jetblue.Config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RSAkeyProps(
        RSAPrivateKey privateKey, RSAPublicKey publicKey
) {
}
