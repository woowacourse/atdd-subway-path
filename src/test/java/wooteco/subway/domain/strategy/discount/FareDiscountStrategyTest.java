package wooteco.subway.domain.strategy.discount;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareDiscountStrategyTest {

    @DisplayName("유아는 100%, 어린이는 350원을 뺀 금액의 50%, 청소년은 350원을 뺀 금액의 20%를 할인해주며"
            + "어른은 할인을 해주지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"5,1250", "12,450", "18,180", "19,0"})
    void calculateDiscountByAge(int age, int expected) {
        FareDiscountStrategy fareDiscountStrategy = FareDiscountStrategyFactory.getFareDiscountStrategy(age);

        int discountAmount = fareDiscountStrategy.calculateDiscount(1250);

        assertThat(discountAmount).isEqualTo(expected);
    }

}
