package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @DisplayName("요금에 음수 값을 넣어 생성하면 예외가 발생한다")
    @Test
    void construct_negative() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Fare(-1000))
                .withMessageContaining("음수");
    }

    @DisplayName("요금에 10 단위가 아닌 값을 넣어 생성하면 예외가 발생한다")
    @Test
    void construct_unit() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Fare(1234))
                .withMessageContaining("단위");
    }
}
