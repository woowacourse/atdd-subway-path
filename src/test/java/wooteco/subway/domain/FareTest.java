package wooteco.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"0, 1250", "9, 1250", "10, 1250", "11, 1350", "15, 1350", "50, 2050", "55, 2150", "58, 2150", "59, 2250"})
    void calculateFare(int distance, int expectedFare) {
        Fare fare = Fare.from(distance);

        Assertions.assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
