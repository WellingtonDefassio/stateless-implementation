package wdefassio.io.statlessauthapi.code.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wdefassio.io.statlessauthapi.code.model.User;
import wdefassio.io.statlessauthapi.infra.exception.AuthenticationException;
import wdefassio.io.statlessauthapi.infra.exception.ValidationException;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app.token.secret-key}")
    private String secretKey;

    public String createToken(User user) {
        var data = new HashMap<String, String>();
        data.put("id", user.getId().toString());
        data.put("username", user.getUsername());

        return Jwts.builder()
                .setClaims(data)
                .setExpiration(expiresAt())
                .signWith(generateSign())
                .compact();
    }


    private Date expiresAt() {
        return Date.from(LocalDateTime.now()
                .plusDays(1L)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public void validateAccessToken(String token) {
        var accessToken = extractToken(token);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(generateSign())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

        } catch (Exception e) {
            throw new AuthenticationException("Invalid token " + e.getMessage());
        }
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }

}
