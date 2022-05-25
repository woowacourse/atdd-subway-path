package wooteco.subway.domain.discountpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountPolicyTest {

    @DisplayName("나이에 따라 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"5, 0", "6, 450", "12, 450", "13, 720", "18, 720", "19, 1250", "64, 1250", "65, 0"})
    void calculate(final int age, final int expected) {
        DiscountPolicy discountPolicy = DiscountPolicyFactory.from(age);

        int actual = discountPolicy.calculate(1250);

        assertThat(actual).isEqualTo(expected);
    }
}
