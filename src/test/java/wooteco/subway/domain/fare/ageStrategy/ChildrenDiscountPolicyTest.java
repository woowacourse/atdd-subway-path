package wooteco.subway.domain.fare.ageStrategy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.LowFareException;

class ChildrenDiscountPolicyTest {
    @Test
    @DisplayName("1500원을 청소년할인하면 575원이 나와야 한다.")
    void calculateFare() {
        ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();
        int actual = childrenDiscountPolicy.calculateFare(1500);

        assertThat(actual).isEqualTo(575);
    }

    @Test
    @DisplayName("350원 미만의 금액이 들어오면 예외를 반환해야 한다.")
    void calculateInvalidFare() {
        ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();

        assertThatThrownBy(
            () -> childrenDiscountPolicy.calculateFare(300)
        ).isInstanceOf(LowFareException.class);
    }
}
