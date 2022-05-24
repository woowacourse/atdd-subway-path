package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountPolicyTest {

    @Test
    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20%가 할인된다.")
    void discountTeenTest() {
        int discountValue = DiscountPolicy.getDiscountValue(1350, 13);

        assertThat(discountValue).isEqualTo(1150);
    }

    @Test
    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50%가 할인된다.")
    void discountChildTest() {
        int discountValue = DiscountPolicy.getDiscountValue(1350, 12);

        assertThat(discountValue).isEqualTo(850);
    }

    @ParameterizedTest
    @CsvSource(value = {"1500, 19, 1500", "2000, 30, 2000", "2000, 35, 2000"})
    @DisplayName("어린이 또는 청소년이 아니면 요금 할인 정책이 적용되지 않는다.")
    void noDiscountTest(int fare, int age, int expectedFare) {
        int discountValue = DiscountPolicy.getDiscountValue(fare, age);

        assertThat(discountValue).isEqualTo(expectedFare);
    }

    @Test
    @DisplayName("어린 아이는 운임이 적용되지 않는다.")
    void discountBabyTest() {
        int discountValue = DiscountPolicy.getDiscountValue(1350, 3);

        assertThat(discountValue).isEqualTo(0);
    }

    @Test
    @DisplayName("잘못된 나이를 입력하면 에러가 발생한다.")
    void discountExceptionTest() {
        assertThatThrownBy(() -> DiscountPolicy.getDiscountValue(1350, -1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("나이는 1 이상이어야 합니다.");
    }
}