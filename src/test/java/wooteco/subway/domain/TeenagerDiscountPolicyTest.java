package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TeenagerDiscountPolicyTest {

    @Test
    @DisplayName("청소년은 350원을 공제한 금액의 20%를 할인한다.")
    void teenagerDiscountPolicy() {
        TeenagerDiscountPolicy policy = new TeenagerDiscountPolicy();
        int result = policy.calculate(1250);

        assertThat(result).isEqualTo(720);
    }
}
