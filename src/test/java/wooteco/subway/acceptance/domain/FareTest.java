package wooteco.subway.acceptance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.Fare;

class FareTest {
    @DisplayName("거리에 비례해 계산된 요금이 정확한지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "16,1450", "50,2050", "51,2150", "59, 2250"})
    void calculate_fare_with_distance(int distance, int expectedFare) {
        // when
        Fare createdFare = Fare.from(distance, 0);
        int actual = createdFare.getFare();

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }

    @DisplayName("노선의 추가 요금에 더한 요금이 정확한지 확인한다.")
    @Test
    void calculate_fare_with_line_extra_fare() {
        // when
        final int distance = 10;
        final int expectedFare = 2150;
        Fare createdFare = Fare.from(distance, 900);
        int actual = createdFare.getFare();

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }
}
