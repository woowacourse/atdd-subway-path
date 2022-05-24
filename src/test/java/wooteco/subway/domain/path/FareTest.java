package wooteco.subway.domain.path;

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

    @DisplayName("1000원에서 500원을 빼면 500원이다")
    @Test
    void subtract() {
        Fare fare_1000 = new Fare(1000);
        Fare fare_500 = new Fare(500);
        Fare fare = fare_1000.subtract(fare_500);

        assertThat(fare.getValue()).isEqualTo(500);
    }

    @DisplayName("1000원을 20% 할인하면 800원이다")
    @Test
    void discount() {
        Fare fare_1000 = new Fare(1000);
        Fare fare = fare_1000.discount(0.2);

        assertThat(fare.getValue()).isEqualTo(800);
    }
}
