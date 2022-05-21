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

        Assertions.assertThat(discountAmount).isEqualTo(550);
    }

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%를 할인한다.")
    @Test
    void discountChild() {
        FareDiscountPolicy discountPolicy = new ChildDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(1350);

        Assertions.assertThat(discountAmount).isEqualTo(850);
    }

    @DisplayName("운임 요금이 기본 공제 금액인 350원 이하인 경우 운임 요금만큼 할인한다(청소년).")
    @ParameterizedTest
    @CsvSource(value = {"350,350", "349,349", "400,360"})
    void discountWhenLessThanBaseDiscountAmountWhenTeenager(int amount, int expectedDiscountAmount) {
        FareDiscountPolicy discountPolicy = new TeenagerDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(amount);

        Assertions.assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }

    @DisplayName("운임 요금이 기본 공제 금액인 350원 이하인 경우 운임 요금만큼 할인한다(어린이).")
    @ParameterizedTest
    @CsvSource(value = {"350,350", "349,349", "400,375"})
    void discountWhenLessThanBaseDiscountAmountWhenChile(int amount, int expectedDiscountAmount) {
        FareDiscountPolicy discountPolicy = new ChildDiscountPolicy();

        int discountAmount = discountPolicy.calculateDiscountAmount(amount);

        Assertions.assertThat(discountAmount).isEqualTo(expectedDiscountAmount);
    }
}
