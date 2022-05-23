package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
}