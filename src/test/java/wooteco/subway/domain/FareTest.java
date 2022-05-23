package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("10km 이하일 때는 기본운임을 반환한다.")
    void calculateDefaultFare() {
        Fare fare = Fare.from(10, 0);

        assertThat(fare.getValue()).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과, 50km 이하일 때는 5km마다 100원을 추가한다.")
    void calculateLongFare() {
        Fare fare = Fare.from(23, 0);

        assertThat(fare.getValue()).isEqualTo(1550);
    }

    @Test
    @DisplayName("50km 초과일 때는 8km마다 100원을 추가한다.")
    void calculateTooLongFare() {
        Fare fare = Fare.from(67, 0);

        assertThat(fare.getValue()).isEqualTo(2350);
    }

    @Test
    @DisplayName("추가 운임이 추가될 경우 해당 금액만큼을 추가한다.")
    void calculateExtraFare() {
        Fare fare = Fare.from(10, 50);

        assertThat(fare.getValue()).isEqualTo(1300);
    }
}