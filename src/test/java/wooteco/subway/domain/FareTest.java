package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.Fare;

@DisplayName("Fare 도메인 객체 관련 테스트")
class FareTest {

    @DisplayName("최단거리를 입력하면 요금을 계산한다.")
    @ParameterizedTest(name = "{index} {displayName} distance={0} expectedFare={1}")
    @CsvSource(value = {"1, 1250", "10, 1250"})
    void calculateFareWithDistanceShortThan10km(final int distance, final int expectedFare) {
        assertThat(Fare.calculate(distance, 0)).isEqualTo(expectedFare);
    }

    @DisplayName("10km 에서 50km 사이일 경우 5km 마다 100원이 추가된 요금을 계산한다.")
    @ParameterizedTest(name = "{index} {displayName} distance={0} expectedFare={1}")
    @CsvSource(value = {"11, 1350", "50, 2050"})
    void calculateFareWithDistanceBetween10kmAnd50km(final int distance, final int expectedFare) {
        assertThat(Fare.calculate(distance, 0)).isEqualTo(expectedFare);
    }

    @DisplayName("50km 를 초과할 경우 8km 마다 100원이 추가된 요금을 계산한다.")
    @Test
    void calculateFareWithDistanceOver50() {
        assertThat(Fare.calculate(51, 0)).isEqualTo(2150);
    }

    @DisplayName("경로에 속해있는 노선 중 추가요금이 있는 경우 추가요금도 포함하여 계산한다.")
    @Test
    void calculateFareWithExtraFare() {
        assertThat(Fare.calculate(10, 500)).isEqualTo(1750);
    }
}
