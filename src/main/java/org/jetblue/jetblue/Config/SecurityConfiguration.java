package org.jetblue.jetblue.Config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import org.jetblue.jetblue.Service.Implementation.CustomDetailsUserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final RSAkeyProps rsakeyProps;
    private CustomDetailsUserProvider customDetailsUserProvider;


    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        //noinspection removal
        return http.authorizeRequests(
                        auth -> auth.requestMatchers("/*Airline/**").authenticated().anyRequest().permitAll()

                )
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // MEAN WE DONT CREATE ANY SESSION LATER IN THE APPLICATION
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .build();
    }


    @Bean
    public AuthenticationProvider AuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authProvider.setUserDetailsService(customDetailsUserProvider);
        return authProvider;
    }

    @Bean
    JwtDecoder decoder() {
        return NimbusJwtDecoder.withPublicKey(rsakeyProps.publicKey()).build();
    }

    @Bean
    JwtEncoder encoder() {
        JWK jwk = new RSAKey.Builder(rsakeyProps.publicKey()).privateKey(rsakeyProps.privateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwkSource);
    }


}
