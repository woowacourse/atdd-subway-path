package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.utils.TestConstants.PARAMETERIZED_NAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeDiscountTest {

    @DisplayName("나이에 따라서 해당하는 AgeDiscount를 반환한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"5, BABY", "6, CHILDREN", "12, CHILDREN", "13, TEENAGER", "18, TEENAGER", "19, ADULT"})
    void findAgeDiscount(int age, AgeDiscount expected) {
        AgeDiscount result = AgeDiscount.findAgeDiscount(age);
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("나이에 따라서 다른 할인을 적용한다.")
    @ParameterizedTest(name = PARAMETERIZED_NAME)
    @CsvSource(value = {"BABY, 0", "CHILDREN, 450", "TEENAGER, 720", "ADULT, 1250"})
    void getDiscountedFare(AgeDiscount ageDiscount, int expected) {
        int result = ageDiscount.getDiscountedFare(1250);
        assertThat(result).isEqualTo(expected);
    }
}
