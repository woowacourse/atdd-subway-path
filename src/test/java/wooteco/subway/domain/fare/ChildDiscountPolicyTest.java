package wooteco.subway.domain.fare;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChildDiscountPolicyTest {

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    @Test
    void discountChild() {
        FareDiscountPolicy discountPolicy = new ChildDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(1350);

        Assertions.assertThat(discountAmount).isEqualTo(500);
    }



    @DisplayName("기본 운임요금 이하인 경우 할인하지 않는다.")
    @ParameterizedTest
    @CsvSource(value = {"1249,0", "1250,450"})
    void discountWhenLessThanBaseDiscountAmountWhenChile(int amount, int expectedDiscountAmount) {
        FareDiscountPolicy discountPolicy = new ChildDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(amount);

        Assertions.assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }
}
