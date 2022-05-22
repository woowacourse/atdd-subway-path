package wooteco.subway.domain.pricing.distancepricing.implement;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.domain.pricing.distancepricing.PricingByDistance;

class PricingTenToFiftyTest {

    @DisplayName("요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"3,0", "10,0", "11,100", "15,100", "16,200", "20,200", "21,300", "50,800", "51,800", "103,800"}, delimiter = ',')
    void calculateFee(int distance, int expectFee) {
        PricingByDistance strategy = PricingTenToFifty.of();
        int result = strategy.calculateFee(distance);
        assertThat(result).isEqualTo(expectFee);
    }
}
