package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("10km 이하일 때는 기본운임을 반환한다.")
    void calculateDefaultFare() {
        Fare fare = Fare.from(10, 0, 20);

        assertThat(fare.getValue()).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과, 50km 이하일 때는 5km마다 100원을 추가한다.")
    void calculateLongFare() {
        Fare fare = Fare.from(23, 0, 20);

        assertThat(fare.getValue()).isEqualTo(1550);
    }

    @Test
    @DisplayName("50km 초과일 때는 8km마다 100원을 추가한다.")
    void calculateTooLongFare() {
        Fare fare = Fare.from(67, 0, 20);

        assertThat(fare.getValue()).isEqualTo(2350);
    }

    @Test
    @DisplayName("추가 운임이 추가될 경우 해당 금액만큼을 추가한다.")
    void calculateExtraFare() {
        Fare fare = Fare.from(10, 50, 20);

        assertThat(fare.getValue()).isEqualTo(1300);
    }

    @Test
    @DisplayName("어린이인 경우 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    void calculateChildFare() {
        Fare fare = Fare.from(10, 0, 7);

        assertThat(fare.getValue()).isEqualTo(800);
    }

    @Test
    @DisplayName("청소년인 경우 운임에서 350원을 공제한 금액의 20%를 할인한다.")
    void calculateTeenagerFare() {
        Fare fare = Fare.from(10, 0, 15);

        assertThat(fare.getValue()).isEqualTo(1070);
    }
}