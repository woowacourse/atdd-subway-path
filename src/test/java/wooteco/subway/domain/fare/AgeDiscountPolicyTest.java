package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeDiscountPolicyTest {

    @ParameterizedTest(name = "나이가 {0}살이고 요금이 1350원이면 할인 후의 가격이 {1}이다.")
    @CsvSource({"5, 0", "6, 500", "12, 500", "13, 800", "18, 800", "19, 1350", "64, 1350", "65, 0"})
    void discount(int age, int expected) {
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.of(age);
        assertThat(ageDiscountPolicy.discount(1350)).isEqualTo(expected);
    }
}
