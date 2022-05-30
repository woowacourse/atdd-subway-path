package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.FareFactory;

public class AdultPolicyTest {

    private final int fare = new FareFactory().getFare(58).calculateFare(58, 0);
    private final DiscountPolicyFactory discountPolicyFactory = new DiscountPolicyFactory();

    @Test
    @DisplayName("성인일 경우 요금 할인 정책이 적용되지 않는다.")
    void adultPolicy() {
        final int adultAge = 19;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(adultAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(2150);
    }
}
