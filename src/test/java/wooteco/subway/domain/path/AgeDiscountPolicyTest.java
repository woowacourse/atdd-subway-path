package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeDiscountPolicyTest {

    @DisplayName("나이에 따라 요금을 할인한다.")
    @ParameterizedTest
    @CsvSource(value = {"1350,5,0", "1350,6,500", "1350,12,500", "1350,13,800", "1350,18,800", "1350,19,1350"})
    void calculateByPolicy(long fare, long age, long expected) {
        long actual = AgeDiscountPolicy.calculateByPolicy(fare, new Age(age));
        assertThat(actual).isEqualTo(expected);
    }
}