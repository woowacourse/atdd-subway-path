package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    @ParameterizedTest
    @CsvSource({"11,1350", "15,1350", "16,1450", "50,2050"})
    void calculateFare_middle(int distance, int fare) {
        FareCalculator fareCalculator = new FareCalculator(distance);
        int actualFare = fareCalculator.calculateFare();

        assertThat(fare).isEqualTo(actualFare);
    }

    @DisplayName("경로 거리가 50Km 이상이면 8km당 100원이 부과된다.")
    @ParameterizedTest
    @CsvSource({"51,2150", "58,2150", "59,2250"})
    void calculateFare_high(int distance, int fare) {
        FareCalculator fareCalculator = new FareCalculator(distance);
        int actualFare = fareCalculator.calculateFare();

        assertThat(actualFare).isEqualTo(fare);
    }

}
