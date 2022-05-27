package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.fare.Fare;

public class FareTest {

    @ParameterizedTest(name = "{0}km일 때 요금은 {1}원이다")
    @CsvSource({"9,1250", "12,1350", "16,1450", "58,2150", "15.1,1450"})
    void calculateByDistance(double distance, int expected) {
        Fare fare = Fare.of(distance, 0, 24);

        assertThat(fare.getValue()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0}km일 때 추가 요금이 {2}원이면 요금은 {1}원이다")
    @CsvSource({"9,1250,100", "12,1350,200", "16,1450,300", "58,2150,400", "15.1,1450,500"})
    void calculateByDistanceAndExtraFare(double distance, int expected, int extraFare) {
        Fare fare = Fare.of(distance, extraFare, 24);

        assertThat(fare.getValue()).isEqualTo(expected + extraFare);
    }

    @ParameterizedTest(name = "{0}km일 때 나이가 {2}살이면 요금은 {1}원이다")
    @CsvSource({"9,0,0", "12,850,12", "16,1230,18", "58,2150,20", "15.1,1450,20"})
    void calculateByDistanceAndAge(double distance, int expected, int age) {
        Fare fare = Fare.of(distance, 0, age);

        assertThat(fare.getValue()).isEqualTo(expected);
    }
}
