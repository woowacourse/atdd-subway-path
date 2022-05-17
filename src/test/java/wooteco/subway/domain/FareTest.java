package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @Test
    @DisplayName("요금을 계산한다 / 10km 이하")
    void calculateFareUnder10km() {
        Fare fare = new Fare(9, 25);
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 이하")
    void calculateFareUnder50km() {
        Fare fare = new Fare(22, 25);
        assertThat(fare.calculateFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 초과")
    void calculateFareOver50km() {
        Fare fare = new Fare(79, 25);
        assertThat(fare.calculateFare()).isEqualTo(3050);
    }
}
