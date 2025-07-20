package org.jetblue.jetblue.Service.Implementation;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TokenImpl {

    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet
                .builder()
                .issuer("jetblue")
                .issuedAt(now)
                .subject(authentication.getName())
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public Authentication getAuthentication(String token) {
        var jwt = jwtDecoder.decode(token);
        String username = jwt.getSubject();
        var authorities = jwt.getClaimAsStringList("scope").stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);

    }
    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
