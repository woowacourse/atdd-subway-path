package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "10, 1250", "11, 1350", "15, 1350", "50, 2050", "55, 2150", "58, 2150", "59, 2250"})
    void calculateFare(final int distance, final int expectedFare) {
        final Fare fare = Fare.from(distance);

        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
