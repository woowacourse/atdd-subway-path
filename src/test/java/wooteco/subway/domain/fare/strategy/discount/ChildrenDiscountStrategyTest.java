package wooteco.subway.domain.fare.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChildrenDiscountStrategyTest {

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50% 할인이 적용된다.")
    @Test
    void getDiscountedFare() {
        ChildrenDiscountStrategy childrenDiscountStrategy = new ChildrenDiscountStrategy();

        int discountedFare = childrenDiscountStrategy.getDiscountedFare(1250);
        assertThat(discountedFare).isEqualTo(450);
    }
}
