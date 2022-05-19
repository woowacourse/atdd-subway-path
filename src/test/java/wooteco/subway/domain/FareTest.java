package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.Fare;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @Test
    @DisplayName("요금을 계산한다 / 10km 이하")
    void calculateFareUnder10km() {
        Fare fare = Fare.of(9, 25, 0);
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 이하")
    void calculateFareUnder50km() {
        Fare fare = Fare.of(22, 25, 0);
        assertThat(fare.calculateFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 초과")
    void calculateFareOver50km() {
        Fare fare = Fare.of(79, 25, 0);
        assertThat(fare.calculateFare()).isEqualTo(2450);
    }

    @Test
    @DisplayName("요금을 계산한다 / 10km")
    void calculateFareAt10km() {
        Fare fare = Fare.of(10, 25, 0);
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km")
    void calculateFareAt50km() {
        Fare fare = Fare.of(50, 25, 0);
        assertThat(fare.calculateFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 25km")
    void calculateFareAt25km() {
        Fare fare = Fare.of(25, 25, 0);
        assertThat(fare.calculateFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다 / 추가요금 존재")
    void calculateFareWithExtraFare() {
        Fare fare = Fare.of(25, 25, 500);
        assertThat(fare.calculateFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("할인된 요금을 계산한다 / Baby")
    void calculateFareForBaby() {
        Fare fare = Fare.of(25, 2, 0);
        assertThat(fare.calculateFare()).isEqualTo(0);
    }

    @Test
    @DisplayName("할인된 요금을 계산한다 / Child")
    void calculateFareForChild() {
        Fare fare = Fare.of(25, 9, 0);
        assertThat(fare.calculateFare()).isEqualTo(600);
    }

    @Test
    @DisplayName("할인된 요금을 계산한다 / Teenager")
    void calculateFareForTeenager() {
        Fare fare = Fare.of(25, 16, 0);
        assertThat(fare.calculateFare()).isEqualTo(960);
    }
}
