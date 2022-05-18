package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.domain.FeeException;

public class FareTest {

    @Test
    @DisplayName("이동 거리가 10km 이내라면 기본 요금 1,250원이 부과된다.")
    void defaultFare() {
        Fare fare = Fare.from(8);
        Long calFee = fare.getValue();

        // then
        assertThat(calFee).isEqualTo(1250L);
    }

    @Test
    @DisplayName("이동 거리가 10km ~ 50km 사이라면 기본 요금에 5km 마다 100원 추가된 요금이 부과된다.")
    void additionalFare() {
        // given
        Fare fare = Fare.from(12);

        // then
        assertThat(fare.getValue()).isEqualTo(1350L);
    }

    @Test
    @DisplayName("50km을 초과한 거리라면 50km까지 계산된 요금에 8km 마다 100원 추가된 요금이 부과된다.")
    void extraAdditionalFare() {
        // given
        Fare fare = Fare.from(58);

        // then
        assertThat(fare.getValue()).isEqualTo(2150L);
    }

    @Test
    @DisplayName("거리가 0 이하인 경우 예외가 발생한다.")
    void fee_underMinimum_exception() {
        // given
        assertThatThrownBy(() -> Fare.from(0))
                .isInstanceOf(FeeException.class);
    }
}
