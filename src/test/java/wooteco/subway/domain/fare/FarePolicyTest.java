package wooteco.subway.domain.fare;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.Line;

class FarePolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"0, 1250", "9, 1250", "10, 1250", "11, 1350", "15, 1350", "50, 2050", "55, 2150", "58, 2150", "59, 2250"})
    void calculateFareWithDistance(int distance, int expectedFare) {
        Line line1 = new Line("신분당선", "red", 0);
        Line line2 = new Line("2호선", "green", 0);

        FarePolicy farePolicy = new FarePolicyImpl();
        Fare fare = farePolicy.calculateFare(distance, List.of(line1, line2));

        Assertions.assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"1000, 500", "1000, 1000", "500, 1000"})
    void calculateFareWithExtraFare(int line1ExtraFare, int line2ExtraFare) {
        Line line1 = new Line("신분당선", "red", line1ExtraFare);
        Line line2 = new Line("2호선", "green", line2ExtraFare);

        FarePolicy farePolicy = new FarePolicyImpl();
        Fare fare = farePolicy.calculateFare(0, List.of(line1, line2));

        Assertions.assertThat(fare.getValue()).isEqualTo(2250);
    }
}
