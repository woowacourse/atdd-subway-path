package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {

    @ParameterizedTest
    @DisplayName("10km 이내의 거리에 대한 기본 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void Calculate_LessThan10_BasicFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);
        final Fare fare = new Fare(distance);

        // when
        final int actual = fare.calculate();

        // then
        assertThat(actual).isEqualTo(1_250);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 요금을 계산한다.")
    @CsvSource(value = {"11:1350", "12:1350", "49:2050", "50:2050"}, delimiter = ':')
    void Calculate_MoreThan10_ExtraFareReturned(final int distanceValue, final int expectedFare) {
        // given
        final Distance distance = new Distance(distanceValue);
        final Fare fare = new Fare(distance);

        // when
        final int actual = fare.calculate();

        // then
        assertThat(actual).isEqualTo(expectedFare);
    }
}