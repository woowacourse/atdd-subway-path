package wooteco.subway.domain.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.FareFactory;
import wooteco.subway.domain.policy.DiscountPolicy;
import wooteco.subway.domain.policy.DiscountPolicyFactory;

public class StudentsPolicyTest {
    private final int fare = new FareFactory().getFare(58).calculateFare(58, 0);
    private final DiscountPolicyFactory discountPolicyFactory = new DiscountPolicyFactory();


    @Test
    @DisplayName("청소년(13세 이상 19세 미만)일 경우 계산된 금액에서 350을 제외한 금액에 20% 할인된 금액이 부과된다.")
    void studentsPolicy_minAge() {
        final int studentAge = 13;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(studentAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(1440);
    }

    @Test
    @DisplayName("청소년(13세 이상 19세 미만)일 경우 계산된 금액에서 350을 제외한 금액에 20% 할인된 금액이 부과된다.")
    void studentsPolicy_maxAge() {
        final int studentAge = 18;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(studentAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(1440);
    }
}
