package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeTest {

    @DisplayName("연령대 확인하기")
    @ParameterizedTest(name = "{0}살 -> {1}")
    @CsvSource(value = {"CHILD,1", "CHILD,12","YOUTH,13","YOUTH,18", "ADULT,19"})
    void valueOf(Age expected, int age) {
        // given

        // when

        // then
        assertThat(Age.valueOf(age)).isEqualTo(expected);
    }

    @DisplayName("나이별 요금 계산")
    @ParameterizedTest(name = "{0} -> 요금 {1}원 예상")
    @CsvSource(value = {"CHILD,800", "YOUTH,1070", "ADULT,1250"})
    void calculateCostByAge(Age age, int expected) {
        // given

        // when
        int result = age.discountFare(1250);

        // then
        assertThat(result).isEqualTo(expected);
    }
}