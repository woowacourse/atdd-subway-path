package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @DisplayName("연령에 따라 요금이 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {"5,0", "6,450", "13,720", "19,1250"})
    void discountFareByAge(int age, int expectedFare) {
        assertThat(Age.discountFare(age, 1250)).isEqualTo(expectedFare);
    }
}
