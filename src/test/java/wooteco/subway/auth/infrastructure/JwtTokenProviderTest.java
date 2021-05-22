package wooteco.subway.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import wooteco.subway.AcceptanceTest;

import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest extends AcceptanceTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("jwt 토큰을 생성한다.")
    @Test
    void createToken() {
        String jwtToken = jwtTokenProvider.createToken("test");

        String[] splitJwtToken = jwtToken.split("\\.");
        String s = splitJwtToken[1];

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedPayload = decoder.decode(s);

        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        Map<String, Object> payload = jacksonJsonParser.parseMap(
                new String(decodedPayload)
        );

        assertThat(payload.get("sub")).isEqualTo("test");
    }

    @DisplayName("jwt토큰을 검증한다.")
    @Test
    void validateToken() {
        String jwtToken = jwtTokenProvider.createToken("test");

        assertThat(jwtTokenProvider.validateToken(jwtToken)).isTrue();
    }
}