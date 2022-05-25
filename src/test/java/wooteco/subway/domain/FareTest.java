package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.distance.Distance;
import wooteco.subway.exception.IllegalInputException;

public class FareTest {

    private static final int EXTRA_FARE = 300;
    private static final int BABY_AGE = 4;
    private static final int KIDS_AGE = 9;
    private static final int TEENAGER_AGE = 18;
    private static final int ADULT_AGE = 30;

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
    @DisplayName("10km 이내의 거리에 대한 영유아 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void NewFare_LessThan10kmWithBaby_ZeroFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, BABY_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("10km 이내의 거리에 대한 어린이 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void NewFare_LessThan10kmWithKids_DiscountFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, KIDS_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(600);
    }

    @ParameterizedTest
    @DisplayName("10km 이내의 거리에 대한 청소년 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void NewFare_LessThan10kmWithTeenager_DiscountFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, TEENAGER_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(960);
    }

    @ParameterizedTest
    @DisplayName("10km 이내의 거리에 대한 성인 요금을 계산한다.")
    @ValueSource(ints = {1, 2, 9, 10})
    void NewFare_LessThan10kmWithAdult_BasicFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, ADULT_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(1_250 + EXTRA_FARE);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 영유아 요금을 계산한다.")
    @ValueSource(ints = {11, 12, 49, 50})
    void NewFare_MoreThan10WithBaby_ZeroFareReturned(final int distanceValue) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, BABY_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 어린이 요금을 계산한다.")
    @CsvSource(value = {"11:650", "12:650", "49:1000", "50:1000"}, delimiter = ':')
    void NewFare_MoreThan10WithKids_FareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, KIDS_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 청소년 요금을 계산한다.")
    @CsvSource(value = {"11:1040", "12:1040", "49:1600", "50:1600"}, delimiter = ':')
    void NewFare_MoreThan10WithTeenager_FareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, TEENAGER_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("10km ~ 50km 사이의 거리에 대한 성인 요금을 계산한다.")
    @CsvSource(value = {"11:1650", "12:1650", "49:2350", "50:2350"}, delimiter = ':')
    void Calculate_MoreThan10WithAdult_FareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, ADULT_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("50km 초과 거리에 대한 영유아 요금을 계산한다.")
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void NewFare_MoreThan50WithBaby_ZeroFareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, BABY_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(0);
    }

    @ParameterizedTest
    @DisplayName("50km 초과 거리에 대한 어린이 요금을 계산한다.")
    @CsvSource(value = {"51:1050", "58:1050", "59:1100"}, delimiter = ':')
    void NewFare_MoreThan50WithKids_FareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, KIDS_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("50km 초과 거리에 대한 청소년 요금을 계산한다.")
    @CsvSource(value = {"51:1680", "58:1680", "59:1760"}, delimiter = ':')
    void NewFare_MoreThan50WithTeenager_FareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, TEENAGER_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest
    @DisplayName("50km 초과 거리에 대한 성인 요금을 계산한다.")
    @CsvSource(value = {"51:2450", "58:2450", "59:2550"}, delimiter = ':')
    void NewFare_MoreThan50WithAdult_ExtraFareReturned(final int distanceValue, final int expected) {
        // given
        final Distance distance = new Distance(distanceValue);

        // when
        final Fare actual = Fare.from(distance, EXTRA_FARE, ADULT_AGE);

        // then
        assertThat(actual.getValue()).isEqualTo(expected);
    }
}
