package wooteco.subway.domain.fare;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareDiscountPolicyTest {

    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20%를 할인한다.")
    @Test
    void discountTeenager() {
        FareDiscountPolicy discountPolicy = new TeenagerDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(1350);

        Assertions.assertThat(discountAmount).isEqualTo(200);
    }

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    @Test
    void discountChild() {
        FareDiscountPolicy discountPolicy = new ChildDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(1350);

        Assertions.assertThat(discountAmount).isEqualTo(500);
    }

    @DisplayName("기본 운임요금 이하인 경우 할인하지 않는다(청소년).")
    @ParameterizedTest
    @CsvSource(value = {"1249,0", "1250,180"})
    void discountWhenLessThanBaseDiscountAmountWhenTeenager(int amount, int expectedDiscountAmount) {
        FareDiscountPolicy discountPolicy = new TeenagerDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(amount);

        Assertions.assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }

    @DisplayName("기본 운임요금 이하인 경우 할인하지 않는다(어린이).")
    @ParameterizedTest
    @CsvSource(value = {"1249,0", "1250,450"})
    void discountWhenLessThanBaseDiscountAmountWhenChile(int amount, int expectedDiscountAmount) {
        FareDiscountPolicy discountPolicy = new ChildDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(amount);

        Assertions.assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }
}
