package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareTest {

    @Test
    @DisplayName("요금을 계산한다 / 10km 이하")
    void calculateFareUnder10km() {
        Fare fare = new Fare(9, 25, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 이하")
    void calculateFareUnder50km() {
        Fare fare = new Fare(22, 25, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km 초과")
    void calculateFareOver50km() {
        Fare fare = new Fare(79, 25, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(3050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 10km")
    void calculateFareAt10km() {
        Fare fare = new Fare(10, 25, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금을 계산한다 / 50km")
    void calculateFareAt50km() {
        Fare fare = new Fare(50, 25, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 25km")
    void calculateFareAt25km() {
        Fare fare = new Fare(25, 25, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다 / 추가요금 존재")
    void calculateFareWithExtraFare() {
        Fare fare = new Fare(25, 25, 500, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(2050);
    }

    @Test
    @DisplayName("요금을 계산한다 / 어린이 요금 할인")
    void calculateFareWithChildDiscount() {
        Fare fare = new Fare(11, 6, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(500);
    }

    @Test
    @DisplayName("요금을 계산한다 / 청소년 요금 할인")
    void calculateFareWithAdolescentDiscount() {
        Fare fare = new Fare(11, 13, 0, new AgeDiscountStrategy());
        assertThat(fare.calculateFare()).isEqualTo(800);
    }
}
