package wooteco.subway.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.Fare;

public class FareByDistancePolicyTest {

    @ParameterizedTest
    @DisplayName("10km 이내의 거리에 대한 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void Apply_LessThan10km_BasicFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);
        final Fare expected = new Fare(1_250);

        // when
        final Fare actual = FareByDistancePolicy.apply(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 요금을 계산한다.")
    @CsvSource(value = {"11:1350", "12:1350", "49:2050", "50:2050"}, delimiter = ':')
    void Apply_MoreThan10_FareReturned(final int distanceValue, final int expectedFareValue) {
        // given
        final Distance distance = new Distance(distanceValue);
        final Fare expected = new Fare(expectedFareValue);

        // when
        final Fare actual = FareByDistancePolicy.apply(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("50km 초과 거리에 대한 요금을 계산한다.")
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void NewFare_MoreThan50WithAdult_ExtraFareReturned(final int distanceValue, final int expectedFareValue) {
        // given
        final Distance distance = new Distance(distanceValue);
        final Fare expected = new Fare(expectedFareValue);

        // when
        final Fare actual = FareByDistancePolicy.apply(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
