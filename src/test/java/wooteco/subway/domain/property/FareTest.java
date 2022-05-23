package wooteco.subway.domain.property;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.property.fare.Fare;
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
    @DisplayName("기본 운임은 1250원이다.")
    public void constructDefault() {
        // given & when
        Fare fare = new Fare();
        // then
        assertThat(fare.getAmount()).isEqualTo(1250);
    }

    @Test
    @DisplayName("운임을 할인한다.")
    public void discountAmount() {
        // given
        Fare fare = new Fare(1000);
        // when
        final Fare discounted = fare.discount(200);
        // then
        assertThat(discounted.getAmount()).isEqualTo(800);
    }

    @Test
    @DisplayName("운임을 할증한다.")
    public void surchargeAmount() {
        // given
        Fare fare = new Fare(1000);
        // when
        final Fare surcharged = fare.surcharge(300);
        // then
        assertThat(surcharged.getAmount()).isEqualTo(1300);
    }
}