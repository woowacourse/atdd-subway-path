package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeDiscounterTest {

    @DisplayName("나이가 주어지면 해당 조건에 맞게 할인한다.")
    @ParameterizedTest
    @CsvSource({"0,1250,0", "5,1250,0", "6,1250,450", "12,1250,450",
            "13,1250,720", "18,1250,720", "19,1250,1250", "20,1250,1250"})
    void 나이_별_할인(int age, int unDiscountFare, int discountFare) {
        AgeDiscounter ageDiscounter = AgeDiscounter.from(age);

        assertThat(ageDiscounter.discount(unDiscountFare)).isEqualTo(discountFare);
    }
}
