package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;

class FareTest {

    @ParameterizedTest
    @CsvSource(value = {
        "5:80:0",
        "15:0:0",
        "58:80:0",
        "5:10:700",
        "15:10:750",
        "58:10:1150",
        "5:15:1120",
        "15:15:1200",
        "58:15:1840",
        "5:20:1750",
        "15:20:1850",
        "58:20:2650"
    }, delimiter = ':')
    @DisplayName("거리, 노선들의 추가요금, 나이에 따라 요금을 계산한다.")
    void calculateFare(int distance, int age, int expected) {
        Lines lines = new Lines(List.of(
            new Line("1호선", "red", 100),
            new Line("2호선", "green", 500),
            new Line("3호선", "orange", 300)
        ));
        Fare fare = Fare.from(distance, lines, age);
        int actual = fare.getAmount();
        assertThat(actual).isEqualTo(expected);
    }
}
