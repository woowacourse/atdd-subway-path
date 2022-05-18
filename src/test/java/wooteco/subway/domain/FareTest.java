package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @DisplayName("거리가 0km 보다 작은 경우 오류가 발생한다.")
    @Test
    void validateDistance() {
        assertThatThrownBy(() -> new Fare(-10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 음수가 될 수 없습니다.");
    }

    @DisplayName("거리가 10km 이내인 경우 기본운임 1,250을 반환한다.")
    @Test
    void basicFare() {
        Fare fare = new Fare(9);

        assertThat(fare.calculate()).isEqualTo(1250);
    }


    @DisplayName("10km ~ 50km 인 경우 5km 까지 마다 100원씩 추가된다.")
    @Test
    void additionalFareBetween10To50() {
        Fare fare = new Fare(12);

        assertThat(fare.calculate()).isEqualTo(1350);
    }

    @DisplayName("50km 초과인 경우 8km 까지 마다 100원씩 추가된다.")
    @Test
    void additionalFareOver50() {
        Fare fare = new Fare(58);

        assertThat(fare.calculate()).isEqualTo(2150);
    }
}
