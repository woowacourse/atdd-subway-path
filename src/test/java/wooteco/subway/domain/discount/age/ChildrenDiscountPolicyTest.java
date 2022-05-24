package wooteco.subway.domain.discount.age;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ChildrenDiscountPolicyTest {

    private final ChildrenDiscountPolicy childrenDiscountPolicy = new ChildrenDiscountPolicy();

    @ParameterizedTest
    @CsvSource(value = {"1250, 450", "2050, 850"})
    @DisplayName("할인을 적용하면, 금액의 350원을 제외한 나머지 금액의 50퍼센트 만큼을 뺀 가격을 반환한다.")
    void applyDiscount(final int money, final int expected) {
        assertThat(childrenDiscountPolicy.applyDiscount(money)).isEqualTo(expected);
    }

    @Test
    void accept() {
    }
}