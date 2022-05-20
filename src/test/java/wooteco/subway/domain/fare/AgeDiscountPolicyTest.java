package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgeDiscountPolicyTest {

    @DisplayName("13 ~ 18세일 경우 요금의 350원을 제하고 20%를 할인한다.")
    @Test
    void discountTeenAger() {
        assertThat(AgeDiscountPolicy.findPolicy(13).discount(1250)).isEqualTo(720);
    }

    @DisplayName("6 ~ 12세일 경우일요금의 350원을 제하고 50%를 할인한다.")
    @Test
    void discountChildren() {
        assertThat(AgeDiscountPolicy.findPolicy(12).discount(1250)).isEqualTo(450);
    }

    @DisplayName("일반은 할인되지 않는다.")
    @Test
    void noDiscount() {
        assertThat(AgeDiscountPolicy.findPolicy(26).discount(1250)).isEqualTo(1250);
    }
}
