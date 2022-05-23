package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class AgeDiscountPolicyTest {

    @DisplayName("영유아를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4, 5})
    void from_infant(final int infantAge) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.from(infantAge);

        assertThat(ageDiscountPolicy).isEqualTo(AgeDiscountPolicy.INFANT);
    }

    @DisplayName("어린이를 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 10, 11, 12})
    void from_child(final int childAge) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.from(childAge);

        assertThat(ageDiscountPolicy).isEqualTo(AgeDiscountPolicy.CHILD);
    }

    @DisplayName("청소년을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 17, 18})
    void from_teen(final int teenAge) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.from(teenAge);

        assertThat(ageDiscountPolicy).isEqualTo(AgeDiscountPolicy.TEEN);
    }

    @DisplayName("어른을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 20, 30, 50, 100})
    void from_adult(final int adultAge) {
        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.from(adultAge);

        assertThat(ageDiscountPolicy).isEqualTo(AgeDiscountPolicy.ADULT);
    }

    @DisplayName("350원을 공제한 금액을 나이에 따라 할인한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "0,0", "5,0",
            "6,500", "12,500",
            "13,800", "18,800",
            "19,1350", "20,1350", "100,1350"})
    void getDiscountedFare(final int age, final int expected) {

        final AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.from(age);
        final int actual = ageDiscountPolicy.getDiscountedFare(1350);

        assertThat(actual).isEqualTo(expected);
    }
}
