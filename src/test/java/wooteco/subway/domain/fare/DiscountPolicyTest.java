package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountPolicyTest {

    @DisplayName("나이에 따라 해당하는 할인율을 운임에 적용한다.")
    @ParameterizedTest(name = "[{index}] 나이: {0}살, 기존 운임: {1}원, 할인된 운임: {2}원")
    @CsvSource(value = {"5,1250,0", "6,1250,450", "12,1250,450", "13,1250,720", "18,1250,720", "19,1250,1250"})
    void discountWithAge(int ageValue, int originalFareValue, int discountedFareValue) {
        // given
        Age age = new Age(ageValue);
        Fare originalFare = new Fare(originalFareValue);
        DiscountPolicy discountPolicy = DiscountPolicy.from(age);

        // when
        Fare actual = discountPolicy.discount(originalFare);
        Fare expected = new Fare(discountedFareValue);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
