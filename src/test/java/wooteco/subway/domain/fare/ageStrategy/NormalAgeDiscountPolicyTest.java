package wooteco.subway.domain.fare.ageStrategy;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NormalAgeDiscountPolicyTest {
    @Test
    @DisplayName("1500원을 일반 할인하면 1500원이 나와야 한다.")
    void calculateFare() {
        NormalAgeDiscountPolicy normalAgeDiscountPolicy = new NormalAgeDiscountPolicy();
        int actual = normalAgeDiscountPolicy.calculateFare(1500);

        assertThat(actual).isEqualTo(1500);
    }
}
