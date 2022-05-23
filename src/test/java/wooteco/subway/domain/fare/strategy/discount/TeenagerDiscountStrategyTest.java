package wooteco.subway.domain.fare.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeenagerDiscountStrategyTest {

    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20% 할인이 적용된다.")
    @Test
    void getDiscountedFare() {
        TeenagerDiscountStrategy teenagerDiscountStrategy = new TeenagerDiscountStrategy();

        int discountedFare = teenagerDiscountStrategy.getDiscountedFare(1250);
        assertThat(discountedFare).isEqualTo(720);
    }
}
