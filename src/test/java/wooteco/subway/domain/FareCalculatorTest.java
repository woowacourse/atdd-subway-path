package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FareCalculatorTest {

    @Test
    @DisplayName("거리를 입력하면 요금을 계산한다.")
    void calculate() {
        FareCalculator fareCalculator = new FareCalculator();

        int fare = fareCalculator.excute(9);

        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리가 12km이면 추가 운임이 적용되어 요금은 1350원이다.")
    void calculate_12() {
        FareCalculator fareCalculator = new FareCalculator();

        int fare = fareCalculator.excute(12);

        assertThat(fare).isEqualTo(1350);
    }
}
