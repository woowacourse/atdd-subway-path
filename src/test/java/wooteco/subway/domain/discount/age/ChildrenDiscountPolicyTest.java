package wooteco.subway.domain.discount.age;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.discount.DiscountCondition;

class ChildrenDiscountPolicyTest {

    private final ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();

    @ParameterizedTest
    @CsvSource(value = {"1250, 450", "2050, 850"})
    @DisplayName("할인을 적용하면, 금액의 350원을 제외한 나머지 금액의 50퍼센트 만큼을 뺀 가격을 반환한다.")
    void applyDiscount(final int money, final int expected) {
        assertThat(childrenDiscountPolicy.applyDiscount(money)).isEqualTo(expected);
    }


    @ParameterizedTest
    @CsvSource(value = {"5,false", "6,true", "12,true", "13,false"})
    @DisplayName("나이를 입력받아 해당 할인 정책이 적용되는 지 확인한다.")
    void accept(final int age, final boolean expected) {
        final DiscountCondition discountCondition = new DiscountCondition(age, 10);
        assertThat(childrenDiscountPolicy.accept(discountCondition)).isEqualTo(expected);
    }
}