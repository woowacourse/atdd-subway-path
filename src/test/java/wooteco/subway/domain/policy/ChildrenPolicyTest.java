package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.FareFactory;

public class ChildrenPolicyTest {

    private final int fare = new FareFactory().getFare(58).calculateFare(58, 0);
    private final DiscountPolicyFactory discountPolicyFactory = new DiscountPolicyFactory();

    @Test
    @DisplayName("어린이(6세 이상 13세 미만)일 경우 계산된 금액에서 350을 제외한 금액에 20% 할인된 금액이 부과된다.")
    void childrenPolicy_minAge() {
        final int childrenAge = 6;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(childrenAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(900);
    }

    @Test
    @DisplayName("어린이(6세 이상 13세 미만)일 경우 계산된 금액에서 350을 제외한 금액에 20% 할인된 금액이 부과된다.")
    void childrenPolicy_maxAge() {
        final int childrenAge = 12;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(childrenAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(900);
    }
}
