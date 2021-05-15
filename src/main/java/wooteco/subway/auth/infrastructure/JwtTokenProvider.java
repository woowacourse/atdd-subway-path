package wooteco.subway.auth.infrastructure;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wooteco.subway.auth.application.AuthorizedException;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(String payload) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(final String token) {
        validateToken(token);

        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            validateExpiration(claims);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    private void validateExpiration(Jws<Claims> claims) {
        boolean isExpired = claims.getBody().getExpiration().before(new Date());
        if (isExpired) {
            throw new AuthorizedException("기간이 만료된 토큰입니다.");
        }
    }
}

