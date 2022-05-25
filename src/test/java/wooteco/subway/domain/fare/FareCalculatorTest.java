package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;

class FareCalculatorTest {

    @Test
    @DisplayName("조건이 주어졌을 때 요금을 계산한다.")
    void calculate() {
        FareCalculator fareCalculator = new FareCalculator(new FarePolicy(), new AgeDiscountPolicy());

        Line line1 = new Line("2호선", "green", 500);
        Line line2 = new Line("신분당선", "red", 1000);
        FareCondition fareCondition = new FareCondition(10, List.of(line1, line2), 20);

        Fare fare = fareCalculator.calculate(fareCondition);

        assertThat(fare).isEqualTo(new Fare(2250));
    }
}
