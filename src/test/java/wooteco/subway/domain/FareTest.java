package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("10km 미만일 경우 1250을 부과한다.")
    void calculateIfDistanceIsUnderTen() {
        int distance = 9;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km일 경우 1350을 부과한다.")
    void calculateIfDistanceIsTen() {
        int distance = 10;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1350);
    }

    @Test
    @DisplayName("11km일 경우 1350을 부과한다.")
    void calculateIfDistanceIsEleven() {
        int distance = 11;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1350);
    }

    @Test
    @DisplayName("15km일 경우 1350을 부과한다.")
    void calculateIfDistanceIsFifteen() {
        int distance = 15;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1350);
    }

    @Test
    @DisplayName("16km일 경우 1450을 부과한다.")
    void calculateIfDistanceIsSixteen() {
        int distance = 16;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1450);
    }

    @Test
    @DisplayName("21km일 경우 1550을 부과한다.")
    void calculateIfDistanceIsTwentyOne() {
        int distance = 21;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1550);
    }

    @Test
    @DisplayName("49km일 경우 2050을 부과한다.")
    void calculateIfDistanceIsFortyNine() {
        int distance = 49;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km일 경우 2050을 부과한다.")
    void calculateIfDistanceIsFifty() {
        int distance = 50;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(2050);
    }

    @Test
    @DisplayName("58km일 경우 2150을 부과한다.")
    void calculateIfDistanceIsFiftyEight() {
        int distance = 58;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(2150);
    }

    @Test
    @DisplayName("59km일 경우 2250을 부과한다.")
    void calculateIfDistanceIsFiftyNine() {
        int distance = 59;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(2250);
    }
}
