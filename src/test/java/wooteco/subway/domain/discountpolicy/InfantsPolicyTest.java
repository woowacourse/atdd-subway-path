package wooteco.subway.domain.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.FareFactory;
import wooteco.subway.domain.policy.DiscountPolicy;
import wooteco.subway.domain.policy.DiscountPolicyFactory;

public class InfantsPolicyTest {

    private final int fare = new FareFactory().getFare(58).calculateFare(58, 0);
    private final DiscountPolicyFactory discountPolicyFactory = new DiscountPolicyFactory();

    @Test
    @DisplayName("영유아(6세 미만)일 경우 요금이 부과되지 않는다.")
    void infantsPolicy() {
        final int infantAge = 3;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(infantAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(0);
    }
}
