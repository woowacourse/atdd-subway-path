package wooteco.subway.domain.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KidDiscountStrategyTest {

    @DisplayName("50% 가 할인되는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"2000,1175", "2350,1350"})
    void calculateAdolescentDiscountStrategy(int actual, int expected) {
        KidDiscountStrategy discountStrategy = new KidDiscountStrategy();
        assertThat(discountStrategy.calculate(actual)).isEqualTo(expected);
    }

}
