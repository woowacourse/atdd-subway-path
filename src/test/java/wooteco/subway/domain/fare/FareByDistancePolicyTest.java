package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.section.Distance;

class FareByDistancePolicyTest {
    @DisplayName("거리를 전달받아 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "12,1350", "16,1450", "30,1650", "40,1850", "41,1950", "50,2050", "58,2150"})
    void calculateFareWithDistance(int distanceValue, int expectedFare) {
        // given
        Distance distance = new Distance(distanceValue);
        FareByDistancePolicy fareByDistancePolicy = FareByDistancePolicy.from(distance);

        // when
        Fare actual = fareByDistancePolicy.calculate(distance);
        Fare expected = new Fare(expectedFare);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
