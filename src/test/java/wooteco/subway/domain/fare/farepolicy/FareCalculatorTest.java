package wooteco.subway.domain.fare.farepolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareCalculatorTest {

    @DisplayName("거리가 0일때 요금을 반환한다.")
    @Test
    void calculateFreeFare() {
        assertThat(FareCalculator.calculateFare(0)).isEqualTo(0);
    }

    @DisplayName("기본 거리 요금을 반환한다.")
    @Test
    void calculateBasicFare() {
        assertThat(FareCalculator.calculateFare(10)).isEqualTo(1250);
    }

    @DisplayName("첫번째 추가 요금을 반환한다.")
    @Test
    void calculateFirstAddFare() {
        assertThat(FareCalculator.calculateFare(50)).isEqualTo(2050);
    }

    @DisplayName("두번째 추가 요금을 반환한다")
    @Test
    void calculateSecondAddFare() {
        assertThat(FareCalculator.calculateFare(58)).isEqualTo(2150);
    }
}
