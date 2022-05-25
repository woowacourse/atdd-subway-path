package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FareDiscountPolicyFactoryTest {


    @DisplayName("나이에 맞는 요금 할인 객체를 생성한다.")
    @ParameterizedTest
    @MethodSource("generateData")
    void create(int age, Class<FareDiscountPolicy> discountPolicyClass) {
        assertThat(FareDiscountPolicyFactory.create(age)).isInstanceOf(discountPolicyClass);
    }

    @DisplayName("나이가 음수인 경우 요금 할인 객체를 생성할 수 없다.")
    @Test
    void throwExceptionWhenNegativeAge() {
        assertThatThrownBy(() -> FareDiscountPolicyFactory.create(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(5, NoDiscountPolicy.class),
                Arguments.of(6, ChildDiscountPolicy.class),
                Arguments.of(9, ChildDiscountPolicy.class),
                Arguments.of(12, ChildDiscountPolicy.class),
                Arguments.of(13, TeenagerDiscountPolicy.class),
                Arguments.of(15, TeenagerDiscountPolicy.class),
                Arguments.of(18, TeenagerDiscountPolicy.class),
                Arguments.of(19, NoDiscountPolicy.class)
        );
    }
}
