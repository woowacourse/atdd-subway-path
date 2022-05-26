package wooteco.subway.domain.fare.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountCalculatorTest {

    @DisplayName("어른 운임을 계산한다.")
    @Test
    void calculateAdultFare() {
        assertThat(DiscountCalculator.calculateDiscountedFare(20, 1250)).isEqualTo(1250);
    }

    @DisplayName("청소년 운임을 계산한다.")
    @Test
    void calculateTeenagerFare() {
        assertThat(DiscountCalculator.calculateDiscountedFare(18, 1250)).isEqualTo(720);
    }

    @DisplayName("어린이 운임을 계산한다.")
    @Test
    void calculateChildrenFare() {
        assertThat(DiscountCalculator.calculateDiscountedFare(12, 1250)).isEqualTo(450);
    }

    @DisplayName("영유아 및 노인 운임을 계산한다")
    @ParameterizedTest
    @ValueSource(ints = {3, 70})
    void calculateInfantAndOldManFare(int age) {
        assertThat(DiscountCalculator.calculateDiscountedFare(age, 1250)).isEqualTo(0);
    }
}
