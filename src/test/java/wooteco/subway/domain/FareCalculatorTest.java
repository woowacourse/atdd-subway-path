package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    private final FareCalculator fareCalculator = new FareCalculator();

    @DisplayName("0과 자연수 이외의 입력은 예외를 반환한다")
    @ParameterizedTest(name = "허용되지 않는 거리 : {0}")
    @ValueSource(ints = {-1})
    void throwExceptionWhenNotNaturalNumberInput(int distance) {
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> fareCalculator.calculateFare(distance, 0)
        ).isInstanceOf(Exception.class);
    }

    @DisplayName("10km 까지는 기본적으로 1250원이다.")
    @ParameterizedTest(name = "거리가 {0} km 일 때는 1250원이다")
    @ValueSource(ints = {0, 9, 10})
    void calculateFareUntil_10km(int distance) {
        assertThat(fareCalculator.calculateFare(distance, 0)).isEqualTo(1_250);

    }

    @DisplayName("10~50km 까지는 5km 마다 100원씩 증액한다")
    @ParameterizedTest(name = "거리가 {0} km 일 때는 {1} 원이다")
    @CsvSource(value = {"11 - 1350", "15 - 1350", "16 - 1450", "50 - 2050"}, delimiterString = " - ")
    void calculateFareBetween_10km_and_50km(int distance, int fare) {
        assertThat(fareCalculator.calculateFare(distance, 0)).isEqualTo(fare);
    }

    @DisplayName("50km 초과시 8km 마다 100원씩 증액한다")
    @ParameterizedTest(name = "거리가 {0} km 일 때는 {1} 원이다")
    @CsvSource(value = {"51 - 2150", "58 - 2150", "59 - 2250"}, delimiterString = " - ")
    void calculateFareOver_50km(int distance, int fare) {
        assertThat(fareCalculator.calculateFare(distance, 0)).isEqualTo(fare);
    }

    @DisplayName("거리 24km 기준 조회시 어린이일 때 요금을 조회한다")
    @Test
    void calculate_24km_children_age() {
        // (1550 - 350) * 0.5
        assertThat(fareCalculator.calculateFare(24, 10)).isEqualTo(600);
    }

    @DisplayName("거리 24km 기준 조회시 청소년일 때 요금을 조회한다")
    @Test
    void calculate_24km_youth_age() {
        // (1550 - 350) * 0.8
        assertThat(fareCalculator.calculateFare(24, 16)).isEqualTo(960);
    }

    @DisplayName("거리 24km 기준 조회시 성인일 때 요금을 조회한다")
    @Test
    void calculate_24km_adult_age() {
        assertThat(fareCalculator.calculateFare(24, 25)).isEqualTo(1550);
    }
}
