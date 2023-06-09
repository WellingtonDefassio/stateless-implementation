package wdefassio.io.statelessanyapy.core.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wdefassio.io.statelessanyapy.core.dto.AuthUserResponse;
import wdefassio.io.statelessanyapy.infra.exception.AuthenticationException;
import wdefassio.io.statelessanyapy.infra.exception.ValidationException;

import javax.crypto.SecretKey;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class JwtService {


    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;

    @Value("${app.token.secret-key}")
    private String secretKey;


    public AuthUserResponse getAuthenticatedUser(String token) {
        Claims claims = getClaims(token);
        Integer userId = Integer.valueOf((String) claims.get("id"));
        String username = (String) claims.get("username");
        return new AuthUserResponse(userId, username);
    }

    private Claims getClaims(String token) {
        var accessToken = extractToken(token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(generateSign())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

        } catch (Exception e) {
            throw new AuthenticationException("Invalid token " + e.getMessage());
        }
    }

    public void validateAccessToken(String token) {
        getClaims(token);
    }

    private SecretKey generateSign() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
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
