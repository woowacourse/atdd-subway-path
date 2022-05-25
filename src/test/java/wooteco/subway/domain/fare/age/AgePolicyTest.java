package wooteco.subway.domain.fare.age;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgePolicyTest {

    @DisplayName("연령에 따라 각각 다른 할인된 가격을 반환하는지 확인한다.")
    @ParameterizedTest
    @CsvSource(value = {
        "0, 0",
        "5, 0",
        "6, 900",
        "12, 900",
        "13, 1440",
        "18, 1440",
        "19, 2150"
    })
    void ageDiscount(final int age, final int expected) {
        assertThat(AgePolicy.discount(age, 2150)).isEqualTo(expected);
    }
}