package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"9:1750", "10:1750", "11:1850", "49:2550", "50:2550", "51:2650"}, delimiter = ':')
    @DisplayName("거리와 노선들의 추가요금에 따라 요금을 계산한다.")
    void calculateFare1(int distance, int expected) {
        Fare fare = new Fare(1250);
        Lines lines = new Lines(List.of(
            new Line("1호선", "red", 100),
            new Line("2호선", "green", 500),
            new Line("3호선", "orange", 300)
        ));
        int actual = fare.calculateFare(distance, lines);
        assertThat(actual).isEqualTo(expected);
    }
}
