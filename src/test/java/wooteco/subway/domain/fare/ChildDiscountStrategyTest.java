package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChildDiscountStrategyTest {

    @Test
    @DisplayName("입력받은 금액에 대해 어린이 할인이 적용된 금액 반환")
    void discount() {
        int actual = ChildDiscountStrategy.getInstance().discount(1350);

        assertThat(actual).isEqualTo(500);
    }
}