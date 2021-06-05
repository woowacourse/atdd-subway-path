package wooteco.subway.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wooteco.subway.auth.exception.UserLoginFailException;
import wooteco.subway.member.dto.MemberResponse;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    public String createToken(MemberResponse memberResponse) {
        Map<String, Object> payload = createPayload(memberResponse);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(payload)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    private Map<String, Object> createPayload(MemberResponse memberResponse) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", memberResponse.getId());
        payload.put("email", memberResponse.getEmail());
        return payload;
    }

    public Map<String, Object> getPayload(String token) {
        if (validateToken(token)) {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }
        throw new UserLoginFailException();
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

