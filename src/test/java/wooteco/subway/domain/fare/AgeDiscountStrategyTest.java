package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeDiscountStrategyTest {

    private final DiscountStrategy discountStrategy = new AgeDiscountStrategy();

    @Test
    @DisplayName("운임에서 350원을 공제한 금액의 50%할인된 요금을 반환한다. - 어린이")
    void discount_child() {
        int fare = discountStrategy.discount(Age.CHILD, 1350);
        assertThat(fare).isEqualTo(500);
    }

    @Test
    @DisplayName("운임에서 350원을 공제한 금액의 20%할인된 요금을 반환한다. - 청소년")
    void discount_adolescent() {
        int fare = discountStrategy.discount(Age.ADOLESCENT, 1350);
        assertThat(fare).isEqualTo(800);
    }

    @Test
    @DisplayName("요금을 할인하지 않는다.")
    void discount_others() {
        int fare = discountStrategy.discount(Age.OTHERS, 1350);
        assertThat(fare).isEqualTo(1350);
    }
}