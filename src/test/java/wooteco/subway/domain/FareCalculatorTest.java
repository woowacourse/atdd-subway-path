package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.exception.CalculatePathsException;

class FareCalculatorTest {

    @DisplayName("경로 거리가 10Km 이하면 1250이 부과된다.")
    @Test
    void calculateFare_basic() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 10;
        List<Line> lines = List.of(first, second);
        int age = 30;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(1750);
    }

    @DisplayName("경로 거리가 10Km 초과이고 50Km 이하면 5km당 100원이 부과된다.")
    @Test
    void calculateFare_middle() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 50;
        List<Line> lines = List.of(first, second);
        int age = 30;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(2550);
    }

    @DisplayName("경로 거리가 50Km 이상이면 8km당 100원이 부과된다.")
    @Test
    void calculateFare_high() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 30;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(2650);
    }

    @DisplayName("경로 거리가 0과 같으면 예외를 발생한다.")
    @Test
    void calculateFare_exception_zero() {
        double distance = 0;

        assertThatThrownBy(() -> new FareCalculator(distance))
                .isInstanceOf(CalculatePathsException.class)
                .hasMessage("최단 경로의 거리가 0이하 이기 때문에 요금을 계산 할 수 없습니다.");
    }

    @DisplayName("경로 거리가 0과 같으면 예외를 발생한다.")
    @Test
    void calculateFare_exception_underzero() {
        double distance = -3;

        assertThatThrownBy(() -> new FareCalculator(distance))
                .isInstanceOf(CalculatePathsException.class)
                .hasMessage("최단 경로의 거리가 0이하 이기 때문에 요금을 계산 할 수 없습니다.");
    }

    @DisplayName("나이가 13세이면 공제한 금액의 20%가 할인된다.")
    @Test
    void calculateFare_teenager() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 13;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(2190);
    }

    @DisplayName("나이가 18세이면 공제한 금액의 20%가 할인된다.")
    @Test
    void calculateFare_teenager_two() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 18;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(2190);
    }

    @DisplayName("나이가 19세이면 공제한 금액이 할인되지 않는다.")
    @Test
    void calculateFare_adult() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 19;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(2650);
    }

    @DisplayName("나이가 6세이면 공제한 금액의 50%가 할인된다.")
    @Test
    void calculateFare_children() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 6;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(1500);
    }

    @DisplayName("나이가 12세이면 공제한 금액의 50%가 할인된다.")
    @Test
    void calculateFare_children_two() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 12;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(1500);
    }

    @DisplayName("나이가 6살 미만이면 공제된 금액이 할인되지 않는다.")
    @Test
    void calculateFare_under_than_children() {
        Line first = new Line(1L, "12호선", "red", 200);
        Line second = new Line(2L, "13호선", "green", 500);

        double distance = 58;
        List<Line> lines = List.of(first, second);
        int age = 5;

        FareCalculator fareCalculator = new FareCalculator(distance);
        int fare = fareCalculator.calculateFare(lines, age);

        assertThat(fare).isEqualTo(0);
    }
}
