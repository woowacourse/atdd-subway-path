package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.policy.FarePolicy;

class AgeDiscountPolicyTest {

    @Test
    @DisplayName("아기 요금을 계산한다.")
    void calculate_baby() {
        FarePolicy ageDiscountPolicy = PolicyFactory.createAgeDiscount(3);
        assertThat(ageDiscountPolicy.calculate(1250)).isEqualTo(0);
    }

    @Test
    @DisplayName("어린이 요금을 계산한다.")
    void calculate_child() {
        FarePolicy ageDiscountPolicy_6 = PolicyFactory.createAgeDiscount(6);
        FarePolicy ageDiscountPolicy_12 = PolicyFactory.createAgeDiscount(12);

        assertThat(ageDiscountPolicy_6.calculate(1250)).isEqualTo(450);
        assertThat(ageDiscountPolicy_12.calculate(1250)).isEqualTo(450);
    }

    @Test
    @DisplayName("청소년 요금을 계산한다.")
    void calculate_teenager() {
        FarePolicy ageDiscountPolicy_18 = PolicyFactory.createAgeDiscount(18);
        FarePolicy ageDiscountPolicy_19 = PolicyFactory.createAgeDiscount(19);

        assertThat(ageDiscountPolicy_18.calculate(1250)).isEqualTo(720);
        assertThat(ageDiscountPolicy_19.calculate(1250)).isEqualTo(1250);
    }
}
