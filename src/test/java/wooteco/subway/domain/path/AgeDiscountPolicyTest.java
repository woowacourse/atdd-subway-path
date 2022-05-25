package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AgeDiscountPolicyTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("나이에 맞는 할인 정책을 반환한다.")
    void ageDiscountPolicy(int age, AgeDiscountPolicy policy) {
        AgeDiscountPolicy from = AgeDiscountPolicy.from(age);
        assertThat(from).isEqualTo(policy);
    }

    public static Stream<Arguments> ageDiscountPolicy() {
        return Stream.of(
                Arguments.of(5, AgeDiscountPolicy.BABY),
                Arguments.of(6, AgeDiscountPolicy.CHILDREN),
                Arguments.of(12, AgeDiscountPolicy.CHILDREN),
                Arguments.of(13, AgeDiscountPolicy.TEENAGER),
                Arguments.of(18, AgeDiscountPolicy.TEENAGER),
                Arguments.of(19, AgeDiscountPolicy.ADULT)
        );
    }

    @Test
    @DisplayName("아기의 할인금을 계산한다.")
    void discountAmount_Baby() {
        //given
        AgeDiscountPolicy baby = AgeDiscountPolicy.BABY;

        //when
        int discountAmount = baby.discountAmount(1350);

        //then
        assertThat(discountAmount).isEqualTo(1350);
    }

    @Test
    @DisplayName("어린이의 할인금을 계산한다.")
    void discountAmount_Children() {
        //given
        AgeDiscountPolicy policy = AgeDiscountPolicy.CHILDREN;

        //when
        int discountAmount = policy.discountAmount(1350);

        //then
        assertThat(discountAmount).isEqualTo(500);
    }

    @Test
    @DisplayName("청소년의 할인금을 계산한다.")
    void discountAmount_Teenager() {
        //given
        AgeDiscountPolicy policy = AgeDiscountPolicy.TEENAGER;

        //when
        int discountAmount = policy.discountAmount(1350);

        //then
        assertThat(discountAmount).isEqualTo(200);
    }

    @Test
    @DisplayName("성인의 할인금을 계산한다.")
    void discountAmount_Adult() {
        //given
        AgeDiscountPolicy policy = AgeDiscountPolicy.ADULT;

        //when
        int discountAmount = policy.discountAmount(1350);

        //then
        assertThat(discountAmount).isEqualTo(0);
    }
}
