package wooteco.subway.domain.pricing.distancepricing.implement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

class PricingInFirstSectionTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"3,1250", "10,1250", "11,1250", "51,1250", "103,1250"}, delimiter = ',')
    void calculateFee(int distance, int expectFee) {
        PricingBySection strategy = PricingInFirstSection.of();
        int result = strategy.calculateFee(distance);
        assertThat(result).isEqualTo(expectFee);
    }
}
