package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareFactory;
import wooteco.subway.domain.policy.DiscountPolicy;
import wooteco.subway.domain.policy.DiscountPolicyFactory;
import wooteco.subway.exception.domain.DiscountPolicyException;

public class DiscountPolicyTest {

    private final Fare fare = new FareFactory().getFare(58, 0);
    private final DiscountPolicyFactory discountPolicyFactory = new DiscountPolicyFactory();

    @Test
    @DisplayName("성인일 경우 요금 할인 정책이 적용되지 않는다.")
    void adultPolicy() {
        final int adultAge = 19;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(adultAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(2150);
    }

    @Test
    @DisplayName("청소년(13세 이상 19세 미만)일 경우 계산된 금액에서 350을 제외한 금액에 20% 할인된 금액이 부과된다.")
    void studentsPolicy() {
        final int studentAge = 14;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(studentAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(1440);
    }

    @Test
    @DisplayName("어린이(6세 이상 13세 미만)일 경우 계산된 금액에서 350을 제외한 금액에 20% 할인된 금액이 부과된다.")
    void childrenPolicy() {
        final int childrenAge = 6;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(childrenAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(900);
    }

    @Test
    @DisplayName("영유아(6세 미만)일 경우 요금이 부과되지 않는다.")
    void infantsPolicy() {
        final int infantAge = 3;

        DiscountPolicy discountPolicy = discountPolicyFactory.getDiscountPolicy(infantAge);
        int discountedFare = discountPolicy.calculateDiscountFare(fare);

        assertThat(discountedFare).isEqualTo(0);
    }

    @Test
    @DisplayName("거리가 0 이하인 경우 예외가 발생한다.")
    void underMinimumAge_exception() {
        // given
        assertThatThrownBy(() -> discountPolicyFactory.getDiscountPolicy(-1))
                .isInstanceOf(DiscountPolicyException.class);
    }
}

