package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {
    private final List<Integer> lines50ExtraFare = List.of(50, 40);
    private final List<Integer> linesNoExtraFare = List.of(0);

    @Test
    @DisplayName("10km 이하일 때는 기본운임을 반환한다.")
    void calculateDefaultFare() {
        Fare fare = Fare.of(10, linesNoExtraFare, 20);

        assertThat(fare.getValue()).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과, 50km 이하일 때는 5km마다 100원을 추가한다.")
    void calculateLongFare() {
        Fare fare = Fare.of(23, linesNoExtraFare, 20);

        assertThat(fare.getValue()).isEqualTo(1550);
    }

    @Test
    @DisplayName("50km 초과일 때는 8km마다 100원을 추가한다.")
    void calculateTooLongFare() {
        Fare fare = Fare.of(67, linesNoExtraFare, 20);

        assertThat(fare.getValue()).isEqualTo(2350);
    }

    @Test
    @DisplayName("추가 운임이 추가될 경우 해당 금액만큼을 추가한다.")
    void calculateExtraFare() {
        Fare fare = Fare.of(10, lines50ExtraFare, 20);

        assertThat(fare.getValue()).isEqualTo(1300);
    }

    @Test
    @DisplayName("어린이인 경우 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    void calculateChildFare() {
        Fare fare = Fare.of(10, linesNoExtraFare, 7);

        assertThat(fare.getValue()).isEqualTo(800);
    }

    @Test
    @DisplayName("청소년인 경우 운임에서 350원을 공제한 금액의 20%를 할인한다.")
    void calculateTeenagerFare() {
        Fare fare = Fare.of(10, linesNoExtraFare, 15);

        assertThat(fare.getValue()).isEqualTo(1070);
    }
}