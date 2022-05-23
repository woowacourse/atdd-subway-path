package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareFactory;
import wooteco.subway.exception.domain.FeeException;

public class FareTest {

    private final FareFactory fareFactory = new FareFactory();

    @Test
    @DisplayName("이동 거리가 10km 이내라면 기본 요금 1,250원이 부과된다.")
    void defaultFare() {
        Fare fare = fareFactory.getFare(8, 0);

        // then
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("이동 거리가 10km 이내라면 기본 요금 1,250원이 부과된다.")
    void defaultFare_10km() {
        Fare fare = fareFactory.getFare(10, 0);

        // then
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("이동 거리가 10km ~ 50km 사이라면 기본 요금에 5km 마다 100원 추가된 요금이 부과된다.")
    void additionalFare() {
        // given
        Fare fare = fareFactory.getFare(12, 0);
        // then
        assertThat(fare.calculateFare()).isEqualTo(1350);
    }

    @Test
    @DisplayName("이동 거리가 10km ~ 50km 사이라면 기본 요금에 5km 마다 100원 추가된 요금이 부과된다.")
    void additionalFare2() {
        // given
        Fare fare = fareFactory.getFare(50, 0);

        // then
        assertThat(fare.calculateFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km을 초과한 거리라면 50km까지 계산된 요금에 8km 마다 100원 추가된 요금이 부과된다.")
    void extraAdditionalFare() {
        // given
        Fare fare = fareFactory.getFare(58, 0);
        // then
        assertThat(fare.calculateFare()).isEqualTo(2150);
    }

    @Test
    @DisplayName("거리가 0 이하인 경우 예외가 발생한다.")
    void fee_underMinimum_exception() {
        // given
        assertThatThrownBy(() -> fareFactory.getFare(0, 0))
                .isInstanceOf(FeeException.class);
    }
}
