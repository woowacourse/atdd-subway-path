package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.pricing.implement.DistanceProportionalPricingStrategy;
import wooteco.subway.domain.pricing.PricingStrategy;

public class DistanceProportionalPricingStrategyTest {

    @DisplayName("요금 계산하기")
    @ParameterizedTest(name = "{0} km -> 요금 {1}원 예상")
    @CsvSource(value = {"5, 1250", "10, 1250", "11, 1350", "15, 1350", "16, 1450",
            "50, 2050", "51, 2150", "58, 2150", "59, 2250"})
    void calculateScore2(int distance, int expected) {
        // given
        List<Section> sections = List.of(new Section(1L, 1L, 1L, 2L, distance));
        PricingStrategy strategy = new DistanceProportionalPricingStrategy();

        // when
        int result = strategy.calculateFee(sections);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
