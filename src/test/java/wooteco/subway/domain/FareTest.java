package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.DomainException;

public class FareTest {

    @Test
    @DisplayName("10km 이내라면 기본 요금 1,250원이다.")
    void defaultFee() {
        Fare fare = Fare.from(8);
        Long calFee = fare.getValue();

        // then
        assertThat(calFee).isEqualTo(1250L);
    }

    @Test
    @DisplayName("10km ~ 50km 사이의 거리는 5km 마다 100원 추가")
    void additionalFee() {
        // given
        Fare fare = Fare.from(12);

        // then
        assertThat(fare.getValue()).isEqualTo(1350L);
    }

    @Test
    @DisplayName("50km를 넘는 거리는 8km 마다 100원 추가")
    void additionalFee_over_fifth() {
        // given
        Fare fare = Fare.from(58);

        // then
        assertThat(fare.getValue()).isEqualTo(2150L);
    }

    @Test
    @DisplayName("거리가 0 이하인 경우 예외 발생")
    void fee_underMinimum() {
        // given
        assertThatThrownBy(() -> Fare.from(0))
                .isInstanceOf(DomainException.class);
    }
}
