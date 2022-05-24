package wooteco.subway.domain.fare.ageStrategy;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.exception.LowFareException;

class TeenagerDiscountPolicyTest {
    @Test
    @DisplayName("1500원을 청소년할인하면 920원이 나와야 한다.")
    void calculateFare() {
        TeenagerDiscountPolicy teenagerDiscountPolicy = new TeenagerDiscountPolicy();
        int actual = teenagerDiscountPolicy.calculateFare(1500);

        assertThat(actual).isEqualTo(920);
    }

    @Test
    @DisplayName("350원 미만의 금액이 들어오면 예외를 반환해야 한다.")
    void calculateInvalidFare() {
        TeenagerDiscountPolicy teenagerDiscountPolicy = new TeenagerDiscountPolicy();

        assertThatThrownBy(
            () -> teenagerDiscountPolicy.calculateFare(300)
        ).isInstanceOf(LowFareException.class);
    }
}
