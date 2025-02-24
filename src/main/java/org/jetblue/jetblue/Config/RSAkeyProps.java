package org.jetblue.jetblue.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RSAkeyProps(
        RSAPrivateKey privateKey,
        RSAPublicKey publicKey
) {
}
