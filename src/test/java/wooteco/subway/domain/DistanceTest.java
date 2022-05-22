package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DistanceTest {
    private final Distance tenKilometer = Distance.fromKilometer(10);
    private final Distance fiveKilometer = Distance.fromKilometer(5);

    @DisplayName("값으로 100미터를 넣어서 Distance를 생성하면 value는 0.1이다")
    @Test
    void fromMeter_100() {
        Distance distance = Distance.fromMeter(100);
        assertThat(distance.getValue()).isEqualTo(0.1);
    }

    @DisplayName("음수 값으로 거리를 생성하면 예외가 발생한다")
    @Test
    void fromKilometer_negative() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Distance.fromKilometer(-1))
                .withMessageContaining("음수");
    }

    @DisplayName("10km에 5km를 더하면 15km이다.")
    @Test
    void add_5_to_10_15() {
        Distance result = tenKilometer.add(fiveKilometer);

        assertThat(result.getValue()).isEqualTo(15);
    }

    @DisplayName("10km에서 5km를 빼면 5km이다.")
    @Test
    void subtract_5_from_10_5() {
        Distance result = tenKilometer.subtract(fiveKilometer);

        assertThat(result.getValue()).isEqualTo(5);
    }

    @DisplayName("원래 거리보다 빼려는 거리가 더 크면 얘외가 발생한다.")
    @Test
    void subtract_exception_bigger_value() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> fiveKilometer.subtract(tenKilometer))
                .withMessageContaining("더 커서");
    }

    @DisplayName("5km는 10km보다 작은 거리이다.")
    @Test
    void smallerThan_true() {
        assertThat(fiveKilometer.isSmallerThan(tenKilometer)).isTrue();
    }
}
