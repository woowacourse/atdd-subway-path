package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.strategy.discount.AdolescentDiscountStrategy;

class AdolescentDiscountStrategyTest {

    @DisplayName("20% 가 할인되는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"2000,1670", "1700,1430"})
    void calculateAdolescentDiscountStrategy(int actual, int expected) {
        AdolescentDiscountStrategy discountStrategy = new AdolescentDiscountStrategy();
        assertThat(discountStrategy.calculate(actual)).isEqualTo(expected);
    }
}
