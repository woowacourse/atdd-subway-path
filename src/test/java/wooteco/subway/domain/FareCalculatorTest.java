package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.CalculatePathsException;

class FareCalculatorTest {

    @DisplayName("경로 거리가 10Km 이하면 1250이 부과된다.")
    @Test
    void calculateFare_basic() {
        double distance = 10;
        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare();

        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("경로 거리가 10Km 초과이고 50Km 이하면 5km당 100원이 부과된다.")
    @Test
    void calculateFare_middle() {
        double distance = 50;
        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare();

        assertThat(fare).isEqualTo(2050);
    }

    @DisplayName("경로 거리가 50Km 이상이면 8km당 100원이 부과된다.")
    @Test
    void calculateFare_high() {
        double distance = 58;
        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare();

        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("경로 거리가 0과 같으면 예외를 발생한다.")
    @Test
    void calculateFare_exception_zero() {
        double distance = 0;

        assertThatThrownBy(() -> new FareCalculator(distance))
                .isInstanceOf(CalculatePathsException.class)
                .hasMessage("최단 경로의 거리가 0이하 이기 때문에 요금을 계산 할 수 없습니다.");
    }

    @DisplayName("경로 거리가 0과 같으면 예외를 발생한다.")
    @Test
    void calculateFare_exception_underzero() {
        double distance = -3;

        assertThatThrownBy(() -> new FareCalculator(distance))
                .isInstanceOf(CalculatePathsException.class)
                .hasMessage("최단 경로의 거리가 0이하 이기 때문에 요금을 계산 할 수 없습니다.");
    }
}
