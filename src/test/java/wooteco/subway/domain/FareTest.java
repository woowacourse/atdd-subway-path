package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @Test
    @DisplayName("10km 미만일 경우 1250을 부과한다.")
    void calculateUnderBasicFare() {
        int distance = 1;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km~50km: 5km 까지 마다 100원 추가한다.")
    void calculateFare() {
        int distance = 16;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(1450);
    }

    @Test
    @DisplayName("50km 초과: 8km 까지 마다 100원 추가한다.")
    void calculateFareOver50km() {
        int distance = 108;
        Fare fare = new Fare();
        int expected = fare.calculateFare(distance, new BasicFareStrategy());

        assertThat(expected).isEqualTo(2850);
    }
}
