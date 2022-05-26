package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class AgeDiscountPolicyTest {

    @DisplayName("6세 이상, 13세 미만의 아동은 현재 금액에서 350원을 빼고 50프로 할인")
    @Test
    void childDiscount() {
        AgeDiscountPolicy  ageDiscountPolicy = AgeDiscountPolicy.of(10);

        int actual = ageDiscountPolicy.applyDiscount(1250);
        int expected = (int) ((1250 - 350) * 0.5);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("13세 이상, 19세 미만의 청소년은 현재 금액에서 350원을 빼고 20프로 할인")
    @Test
    void adolescentDiscount() {
        AgeDiscountPolicy  ageDiscountPolicy = AgeDiscountPolicy.of(15);

        int actual = ageDiscountPolicy.applyDiscount(1250);
        int expected = (int) ((1250 - 350) * 0.8);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("6세 미만의 유아는 무료")
    @Test
    void babyFree() {
        AgeDiscountPolicy  ageDiscountPolicy = AgeDiscountPolicy.of(4);

        int actual = ageDiscountPolicy.applyDiscount(1250);
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("65세 이상의 노인도 무료")
    @Test
    void elderlyFree() {
        AgeDiscountPolicy  ageDiscountPolicy = AgeDiscountPolicy.of(70);

        int actual = ageDiscountPolicy.applyDiscount(1250);
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"5,0", "6,450", "12,450", "13,720", "18,720", "19,1250", "64,1250", "65,0"})
    void 경계값_검증(int age, int expected) {
        AgeDiscountPolicy  ageDiscountPolicy = AgeDiscountPolicy.of(age);
        int actual = ageDiscountPolicy.applyDiscount(1250);

        assertThat(actual).isEqualTo(expected);
    }
}
