package wooteco.subway.domain.path.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.fare.policy.ChildrenDiscountPolicy;
import wooteco.subway.domain.path.fare.policy.DiscountPolicy;

class ChildrenDiscountPolicyTest {

    @Test
    @DisplayName("어린이는 350원을 공제한 금액의 20%를 할인한다.")
    void teenagerDiscountPolicy() {
        DiscountPolicy policy = new ChildrenDiscountPolicy();
        int result = policy.calculate(1250);

        assertThat(result).isEqualTo(450);
    }

}
