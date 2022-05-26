package wooteco.subway.domain.path.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FreeDiscountStrategyTest {

    @DisplayName("무료가 되는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"1250,0", "2050,0", "3650,0"})
    void calculateAdolescentDiscountStrategy(int actual, int expected) {
        FreeDiscountStrategy discountStrategy = new FreeDiscountStrategy();
        assertThat(discountStrategy.calculate(actual)).isEqualTo(expected);
    }
}
