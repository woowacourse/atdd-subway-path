package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("기본 운임(10km 이내) 요금을 확인한다.")
    @Test
    public void chargeDefaultFare9() {
        // given
        final Fare fare = Fare.of(9, 0);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(1250);
    }

    @DisplayName("10km는 기본운임을 부과한다.")
    @Test
    public void chargeDefaultFare10() {
        // given
        final Fare fare = Fare.of(10, 0);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(1250);
    }

    @DisplayName("12km는 10km ~ 50km 사이이므로 100원 추가운임을 부과한다.")
    @Test
    public void chargeAdditionalFare100() {
        // given
        final Fare fare = Fare.of(12, 0);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(1350);
    }

    @DisplayName("16km는 10km ~ 50km 사이이므로 200원 추가운임을 부과한다.")
    @Test
    public void chargeAdditionalFare200() {
        // given
        final Fare fare = Fare.of(16, 0);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(1450);
    }

    @DisplayName("58km는 50km 초과이므로 900원 추가운임을 부과한다.")
    @Test
    public void chargeAdditionalFareOver50() {
        // given
        final Fare fare = Fare.of(58, 0);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(2150);
    }

    @DisplayName("50km는 800원 추가운임을 부과한다.")
    @Test
    public void chargeAdditionalFare50() {
        // given
        final Fare fare = Fare.of(50, 0);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(2050);
    }

    @Test
    @DisplayName("거리가 음수일 경우 예외가 발생한다.")
    public void validateDistance() {
        // given
        assertThatThrownBy(() -> Fare.of(-1, 0)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수여야합니다.");
    }

    @Test
    @DisplayName("추가 요금이 있는 노선을 이용할 경우 추가 요금 부과")
    public void chargeAdditionalLineFare() {
        // given
        final Fare fare = Fare.of(50, 500);

        // when
        final int result = fare.getValue();

        // then
        assertThat(result).isEqualTo(2550);
    }
}