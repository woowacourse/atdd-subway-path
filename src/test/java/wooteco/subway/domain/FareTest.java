package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.exception.IllegalInputException;

public class FareTest {

    @ParameterizedTest
    @DisplayName("요금의 금액이 0보다 작으면 예외를 던진다.")
    @ValueSource(ints = {-10, -1})
    void NewFare_LessThan0_ExceptionThrown(final int fareValue) {
        // when, then
        assertThatThrownBy(() -> new Fare(fareValue))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("요금은 0보다 작을 수 없습니다.");
    }

    @ParameterizedTest
    @DisplayName("10km 이내의 거리에 대한 기본 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void Calculate_LessThan10_BasicFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance);

        // then
        assertThat(actual.getValue()).isEqualTo(1_250);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 요금을 계산한다.")
    @CsvSource(value = {"11:1350", "12:1350", "49:2050", "50:2050"}, delimiter = ':')
    void Calculate_MoreThan10_ExtraFareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("50km 초과 거리에 대한 요금을 계산한다.")
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void Calculate_MoreThan50_ExtraFareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }
}
