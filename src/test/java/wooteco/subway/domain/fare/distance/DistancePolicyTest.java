package wooteco.subway.domain.fare.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistancePolicyTest {

    @DisplayName("거리에 따라 각각 해당하는 추가요금이 적용되는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {
        "10, 1250",
        "11, 1350",
        "21, 1550",
        "41, 1950",
        "50, 2050",
        "58, 2150",
        "59, 2250"
    })
    void distanceOverFare(final int distance, final int expected) {
        assertThat(DistancePolicy.calculate(distance)).isEqualTo(expected);
    }
}