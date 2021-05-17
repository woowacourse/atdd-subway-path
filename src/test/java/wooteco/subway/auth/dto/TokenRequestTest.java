package wooteco.subway.auth.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TokenRequestTest {

    @DisplayName("정규 표현식을 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = {"kile32@naver.com", "adfad@gmail.com", "af-_.af@naver.com"})
    void isValidEmail(String email) {
        String pattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+$";

        assertThat(email).matches(pattern);
    }
}
