package wooteco.subway.domain.policy;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.domain.DiscountPolicyException;

public class DiscountPolicyTest {

    private final DiscountPolicyFactory discountPolicyFactory = new DiscountPolicyFactory();

    @Test
    @DisplayName("거리가 0 이하인 경우 예외가 발생한다.")
    void underMinimumAge_exception() {
        // given
        assertThatThrownBy(() -> discountPolicyFactory.getDiscountPolicy(-1))
                .isInstanceOf(DiscountPolicyException.class);
    }
}

