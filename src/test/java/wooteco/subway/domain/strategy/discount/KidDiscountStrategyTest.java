package wooteco.subway.domain.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KidDiscountStrategyTest {

    @DisplayName("50% 가 할인되는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"1250,450", "2350,1000", "3650,1650"})
    void calculateAdolescentDiscountStrategy(int actual, int expected) {
        KidDiscountStrategy discountStrategy = new KidDiscountStrategy();
        assertThat(discountStrategy.calculate(actual)).isEqualTo(expected);
    }
}
