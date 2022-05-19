package wooteco.subway.domain.property;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.exception.NegativeFareException;

class FareTest {

    @Test
    @DisplayName("운임이 음수인 경우 예외를 던진다.")
    public void throwsExceptionWithNegativeAmount() {
        // given  & when
        int amount = -100;

        // then
        assertThatExceptionOfType(NegativeFareException.class)
            .isThrownBy(() -> new Fare(amount));
    }

    @Test
    @DisplayName("기본 요금은 1250원이다.")
    public void defaultConstructor() {
        // given & when
        Fare fare = Fare.ofDefault();

        // then
        assertThat(fare.getAmount()).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150"})
    public void calculateFareByDistance(int distance, int expectedFare) {
        // given
        Fare fare = Fare.ofDefault();

        // when
        Fare calculated = fare.calculate(new Distance(distance));

        // then
        assertThat(calculated.getAmount()).isEqualTo(expectedFare);
    }
}