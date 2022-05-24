package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.fare.policy.DiscountPolicy;
import wooteco.subway.domain.fare.policy.TeenagerDiscountPolicy;

public class TeenagerDiscountPolicyTest {

    @Test
    @DisplayName("청소년은 350원을 공제한 금액의 20%를 할인한다.")
    void teenagerDiscountPolicy() {
        DiscountPolicy policy = new TeenagerDiscountPolicy();
        int result = policy.calculate(1250);

        assertThat(result).isEqualTo(720);
    }
}
