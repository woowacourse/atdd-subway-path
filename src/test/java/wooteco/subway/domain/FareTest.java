package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest
    @DisplayName("거리에 따른 추가 요금이 부과된 요금을 계산한다.")
    @CsvSource(
            value = {"9:1250", "10:1250", "11:1350", "12:1350", "49:2050", "50:2050", "51:2150", "58:2150", "59:2250"},
            delimiter = ':'
    )
    void AddByDistance(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);
        final Fare fare = Fare.from(0);

        // when
        final Fare actual = fare.addExtraFareByDistance(distance);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("나이에 따른 할인된 요금을 계산한다.")
    @CsvSource(
            value = {"1:2350", "5:2350", "6:1000", "12:1000", "13:1600", "18:1600", "19:2350"},
            delimiter = ':'
    )
    void DiscountByAge(final int age, final int expected) {
        // given
        final Fare fare = Fare.from(1100);

        // when
        final Fare actual = fare.discountByAge(age);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }
}