package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.policy.age.AdultDiscountPolicy;
import wooteco.subway.domain.fare.policy.age.AgeDiscountPolicyGenerator;
import wooteco.subway.domain.fare.policy.age.BabyDiscountPolicy;
import wooteco.subway.domain.fare.policy.age.ChildDiscountPolicy;
import wooteco.subway.domain.fare.policy.age.TeenagerDiscountPolicy;

class AgeDiscountPolicyGeneratorTest {
    @Test
    @DisplayName("아기 요금할인 정책을 확인한다.")
    void of_baby() {
        assertThat(AgeDiscountPolicyGenerator.of(0))
                .isInstanceOf(BabyDiscountPolicy.class);
        assertThat(AgeDiscountPolicyGenerator.of(5))
                .isInstanceOf(BabyDiscountPolicy.class);
    }

    @Test
    @DisplayName("어린이 요금할인 정책을 확인한다.")
    void of_child() {
        assertThat(AgeDiscountPolicyGenerator.of(6))
                .isInstanceOf(ChildDiscountPolicy.class);
        assertThat(AgeDiscountPolicyGenerator.of(12))
                .isInstanceOf(ChildDiscountPolicy.class);
    }

    @Test
    @DisplayName("청소년 요금할인 정책을 확인한다.")
    void of_teenager() {
        assertThat(AgeDiscountPolicyGenerator.of(13))
                .isInstanceOf(TeenagerDiscountPolicy.class);
        assertThat(AgeDiscountPolicyGenerator.of(18))
                .isInstanceOf(TeenagerDiscountPolicy.class);
    }

    @Test
    @DisplayName("어른 요금할인 정책을 확인한다.")
    void of_adult() {
        assertThat(AgeDiscountPolicyGenerator.of(19))
                .isInstanceOf(AdultDiscountPolicy.class);
        assertThat(AgeDiscountPolicyGenerator.of(60))
                .isInstanceOf(AdultDiscountPolicy.class);
    }
}
