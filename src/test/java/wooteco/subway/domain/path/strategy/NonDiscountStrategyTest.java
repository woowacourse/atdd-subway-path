package wooteco.subway.domain.path.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NonDiscountStrategyTest {

    @DisplayName("할인이 적용되지 않고, 기존 금액이 반환되는지 확인")
    @ParameterizedTest
    @ValueSource(ints = {1250, 2350, 3650})
    void calculateAdolescentDiscountStrategy(int expected) {
        NonDiscountStrategy discountStrategy = new NonDiscountStrategy();
        assertThat(discountStrategy.calculate(expected)).isEqualTo(expected);
    }
}
