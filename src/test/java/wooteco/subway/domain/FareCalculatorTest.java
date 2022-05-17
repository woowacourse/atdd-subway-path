package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}
