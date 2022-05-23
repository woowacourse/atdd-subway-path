package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeDiscountPolicyTest {

    @ParameterizedTest
    @CsvSource(value = {"5,OTHER", "6,CHILDREN", "12,CHILDREN", "13,TEENAGER", "18,TEENAGER", "19,OTHER"})
    @DisplayName("나이를 입력하면 나이에 맞는 할인 정책을 반환한다.")
    void createByAge(final int age, final String rawAgeDiscountPolicy) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.createByAge(age);

        assertThat(ageDiscountPolicy).isSameAs(AgeDiscountPolicy.valueOf(rawAgeDiscountPolicy));
    }

    @ParameterizedTest
    @CsvSource(value = {"OTHER,1250,1250", "CHILDREN,1250,800", "TEENAGER,1250,1070"})
    void applyDiscount(final String rawAgeDiscountPolicy, final int money, final int expectedDiscountedMoney) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.valueOf(rawAgeDiscountPolicy);

        assertThat(ageDiscountPolicy.applyDiscount(money)).isEqualTo(expectedDiscountedMoney);
    }
}