package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @Test
    @DisplayName("10km 이하일 때는 기본운임을 반환한다.")
    void calculateDefaultFare() {
        Fare fare = Fare.from(10);

        assertThat(fare.getValue()).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km 초과, 50km 이하일 때는 5km마다 100원을 추가한다.")
    void calculateLongFare() {
        Fare fare = Fare.from(23);

        assertThat(fare.getValue()).isEqualTo(1550);
    }

    @Test
    @DisplayName("50km 초과일 때는 8km마다 100원을 추가한다.")
    void calculateTooLongFare() {
        Fare fare = Fare.from(67);

        assertThat(fare.getValue()).isEqualTo(2350);
    }
}