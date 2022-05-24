package wooteco.subway.domain.fare.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AllDiscountStrategyTest {

    @DisplayName("100% 할인된 금액을 반환한다.")
    @Test
    void getDiscountedFare() {
        AllDiscountStrategy allDiscountStrategy = new AllDiscountStrategy();

        int discountedFare = allDiscountStrategy.getDiscountedFare(1000);
        assertThat(discountedFare).isZero();
    }
}
