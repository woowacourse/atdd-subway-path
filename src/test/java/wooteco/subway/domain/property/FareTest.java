package wooteco.subway.domain.property;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}