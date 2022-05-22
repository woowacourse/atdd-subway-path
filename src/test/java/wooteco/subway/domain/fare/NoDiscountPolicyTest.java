package wooteco.subway.domain.fare;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NoDiscountPolicyTest {


    @DisplayName("어떠한 할인 조건에도 포함되지 않는 경우 할인하지 않는다")
    @ParameterizedTest
    @CsvSource(value = {"1249,0", "1250,0", "3000,0"})
    void discountWhenLessThanBaseDiscountAmountWhenTeenager(int amount, int expectedDiscountAmount) {
        FareDiscountPolicy discountPolicy = new NoDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(amount);

        Assertions.assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }
}
