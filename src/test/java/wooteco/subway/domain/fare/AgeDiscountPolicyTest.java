package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AgeDiscountPolicyTest {

    @DisplayName("13 ~ 18세일 경우 요금의 350원을 제하고 20%를 할인한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void discountTeenAger(int age) {
        assertThat(AgeDiscountPolicy.findPolicy(age).discount(1250)).isEqualTo(720);
    }

    @DisplayName("6 ~ 12세일 경우일요금의 350원을 제하고 50%를 할인한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void discountChildren(int age) {
        assertThat(AgeDiscountPolicy.findPolicy(age).discount(1250)).isEqualTo(450);
    }

    @DisplayName("6세 미만, 65세 이상은 우대 요금 0원이 청구된다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 65, 66, 67})
    void preferentialFare(int age) {
        assertThat(AgeDiscountPolicy.findPolicy(age).discount(1250)).isEqualTo(0);
    }

    @DisplayName("일반은 할인되지 않는다.")
    @Test
    void noDiscount() {
        assertThat(AgeDiscountPolicy.findPolicy(26).discount(1250)).isEqualTo(1250);
    }
}
