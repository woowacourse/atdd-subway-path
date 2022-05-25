package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.fare.DiscountPolicy;

class DiscountPolicyTest {

    @ParameterizedTest(name = "요금 : {0}, 나이 : {1}, 예상금액 : {2}")
    @CsvSource(value = {"1350, 1, 0", "1350, 5, 0",
            "1350, 6, 500", "1350, 12, 500",
            "1350, 13, 800", "1350, 18, 800",
            "1350, 19, 1350", "1350, 64, 1350",
            "1350, 65, 0", "1350, 66, 0"
    })
    void discountPolicyByAge(long fare, long age, long expected) {
        // given & when
        final long actual = DiscountPolicy.calculateFareByAge(fare, age);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
