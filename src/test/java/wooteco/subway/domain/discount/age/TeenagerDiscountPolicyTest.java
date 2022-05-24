package wooteco.subway.domain.discount.age;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.discount.DiscountCondition;

class TeenagerDiscountPolicyTest {

    private final TeenagerDiscountPolicy teenagerDiscountPolicy = new TeenagerDiscountPolicy();

    @ParameterizedTest
    @CsvSource(value = {"1250, 720", "2050, 1360"})
    @DisplayName("할인을 적용하면, 금액의 350원을 제외한 나머지 금액의 20퍼센트 만큼을 뺀 가격을 반환한다.")
    void applyDiscount(final int money, final int expected) {
        assertThat(teenagerDiscountPolicy.applyDiscount(money)).isEqualTo(expected);
    }


    @ParameterizedTest
    @CsvSource(value = {"12,false", "13,true", "18,true", "19,false"})
    @DisplayName("나이를 입력받아 해당 할인 정책이 적용되는 지 확인한다.")
    void accept(final int age, final boolean expected) {
        final DiscountCondition discountCondition = new DiscountCondition(age, 10);
        assertThat(teenagerDiscountPolicy.accept(discountCondition)).isEqualTo(expected);
    }
}