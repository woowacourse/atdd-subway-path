package wooteco.subway.domain.fare.ageStrategy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PreferentialDiscountPolicyTest {
    @Test
    @DisplayName("1500원을 우대 할인하면 0원이 나와야 한다.")
    void calculateFare() {
        PreferentialDiscountPolicy preferentialDiscountPolicy = new PreferentialDiscountPolicy();
        int actual = preferentialDiscountPolicy.calculateFare(1500);

        assertThat(actual).isEqualTo(0);
    }
}
