package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdolescentDiscountStrategyTest {

    @Test
    @DisplayName("입력받은 금액에 대해 청소년 할인이 적용된 금액 반환")
    void discount() {
        int actual = AdolescentDiscountStrategy.getInstance().discount(1350);

        assertThat(actual).isEqualTo(800);
    }
}