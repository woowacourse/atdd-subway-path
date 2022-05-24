package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("1000원에 500원을 더하면 1500원이다")
    @Test
    void sum() {
        Fare fare = Fare.sum(new Fare(1000), new Fare(500));
        assertThat(fare.getValue()).isEqualTo(1500);
    }
}
