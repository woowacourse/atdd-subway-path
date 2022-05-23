package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AgeDiscountStrategyTest {
    @DisplayName("나이에 맞게 할인이 되는지 테스트")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"2,0", "12,500", "7,500", "13,800", "19,1350", "18,800"})
    void discount(String age, String resultFare) {
        AgeDiscountStrategy ageDiscountStrategy = new AgeDiscountStrategy();
        assertThat(ageDiscountStrategy.discount(1350, Integer.parseInt(age))).isEqualTo(Integer.parseInt(resultFare));
    }
}
