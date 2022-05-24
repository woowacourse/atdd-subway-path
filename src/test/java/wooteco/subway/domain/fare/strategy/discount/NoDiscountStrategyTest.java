package wooteco.subway.domain.fare.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NoDiscountStrategyTest {

    @DisplayName("할인을 적용하지 않는다.")
    @Test
    void getDiscountedFare() {
        NoDiscountStrategy noDiscountStrategy = new NoDiscountStrategy();

        int discountedFare = noDiscountStrategy.getDiscountedFare(1000);
        assertThat(discountedFare).isEqualTo(1000);
    }
}
