package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareCalculatorTest {

    @Test
    @DisplayName("거리별 요금테스트1: 10km 미만일 경우 1250을 부과한다.")
    void calculateIfDistanceIsUnderTen() {
        int distance = 9;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리별 요금테스트2: 10km일 경우 1250을 부과한다.")
    void calculateIfDistanceIsTen() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리별 요금테스트3: 11km일 경우 1350을 부과한다.")
    void calculateIfDistanceIsEleven() {
        int distance = 11;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리별 요금테스트4: 15km일 경우 1350을 부과한다.")
    void calculateIfDistanceIsFifteen() {
        int distance = 15;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(1350);
    }

    @Test
    @DisplayName("거리별 요금테스트5: 16km일 경우 1450을 부과한다.")
    void calculateIfDistanceIsSixteen() {
        int distance = 16;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);
        assertThat(expected).isEqualTo(1450);
    }

    @Test
    @DisplayName("거리별 요금테스트6: 21km일 경우 1550을 부과한다.")
    void calculateIfDistanceIsTwentyOne() {
        int distance = 21;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(1550);
    }

    @Test
    @DisplayName("거리별 요금테스트7: 49km일 경우 2050을 부과한다.")
    void calculateIfDistanceIsFortyNine() {
        int distance = 49;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(2050);
    }

    @Test
    @DisplayName("거리별 요금테스트8: 50km일 경우 2050을 부과한다.")
    void calculateIfDistanceIsFifty() {
        int distance = 50;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(2050);
    }

    @Test
    @DisplayName("거리별 요금테스트9: 58km일 경우 2150을 부과한다.")
    void calculateIfDistanceIsFiftyEight() {
        int distance = 58;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(2150);
    }

    @Test
    @DisplayName("거리별 요금테스트10: 59km일 경우 2250을 부과한다.")
    void calculateIfDistanceIsFiftyNine() {
        int distance = 59;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(2250);
    }

    @Test
    @DisplayName("연령별 요금테스트1: 5세일 경우 무료이다.")
    void calculateIfAgeIsFive() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 5);

        assertThat(expected).isZero();
    }

    @Test
    @DisplayName("연령별 요금테스트2: 6세일 경우 운임에서 350원을 공제한 금액의 50% 할인한다.")
    void calculateIfAgeIsSix() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 6);

        assertThat(expected).isEqualTo(450);
    }

    @Test
    @DisplayName("연령별 요금테스트3: 12세일 경우 운임에서 350원을 공제한 금액의 50% 할인한다.")
    void calculateIfAgeIsTwelve() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 12);

        assertThat(expected).isEqualTo(450);
    }

    @Test
    @DisplayName("연령별 요금테스트4: 13세일 경우 운임에서 350원을 공제한 금액의 20% 할인한다.")
    void calculateIfAgeIsThirteen() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 13);

        assertThat(expected).isEqualTo(720);
    }

    @Test
    @DisplayName("연령별 요금테스트5: 19세일 경우 운임에서 350원을 공제한 금액의 20% 할인한다.")
    void calculateIfAgeIsNineteen() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 19);

        assertThat(expected).isEqualTo(720);
    }

    @Test
    @DisplayName("연령별 요금테스트6: 20세일 경우 운임요금의 100%를 받는다.")
    void calculateIfAgeIsTwenty() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 20);

        assertThat(expected).isEqualTo(1250);
    }

    @Test
    @DisplayName("연령별 요금테스트7: 64세일 경우 운임요금의 100%를 받는다.")
    void calculateIfAgeIsSixtyFour() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 64);

        assertThat(expected).isEqualTo(1250);
    }

    @Test
    @DisplayName("연령별 요금테스트8: 65세일 경우 무료이다.")
    void calculateIfAgeIsSixtyFive() {
        int distance = 10;
        FareCalculator fareCalculator = new FareCalculator();
        int expected = fareCalculator.calculateFare(distance, 0, 65);

        assertThat(expected).isZero();
    }
}
