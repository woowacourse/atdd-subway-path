package wooteco.subway.domain.strategy.fare.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountStrategyTest {

    @Test
    @DisplayName("청소년의 할인 금액을 계산한다")
    void calculateTeenagerDiscount() {
        int originalPrice = 1350;

        DiscountStrategy discountStrategy = DiscountStrategyFactory.getDiscountStrategy(13);

        int discountPrice = discountStrategy.calculateDiscount(originalPrice);

        assertThat(discountPrice).isEqualTo(200);
    }

    @Test
    @DisplayName("어린이의 할인 금액을 계산한다")
    void calculateChildDiscount() {
        int originalPrice = 1350;

        DiscountStrategy discountStrategy = DiscountStrategyFactory.getDiscountStrategy(12);

        int discountPrice = discountStrategy.calculateDiscount(originalPrice);

        assertThat(discountPrice).isEqualTo(500);
    }

    @Test
    @DisplayName("할인 대상이 아닌 인원의 할인 금액을 계산한다")
    void calculateOtherDiscount() {
        int originalPrice = 1350;

        DiscountStrategy discountStrategy = DiscountStrategyFactory.getDiscountStrategy(20);

        int discountPrice = discountStrategy.calculateDiscount(originalPrice);

        assertThat(discountPrice).isEqualTo(0);
    }
}
