package wooteco.subway.domain.path.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.path.fare.policy.DiscountPolicy;
import wooteco.subway.domain.path.fare.policy.FreeDiscountPolicy;

public class FreeDiscountPolicyTest {

    @Test
    @DisplayName("유아, 노인은 무임이 적용된다.")
    void teenagerDiscountPolicy() {
        DiscountPolicy policy = new FreeDiscountPolicy();
        int result = policy.calculate(1250);

        assertThat(result).isEqualTo(0);
    }
}
