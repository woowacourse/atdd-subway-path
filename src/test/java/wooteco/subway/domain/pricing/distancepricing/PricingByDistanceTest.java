package wooteco.subway.domain.pricing.distancepricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingOverFifty;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingTenToFifty;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingUnderTen;

class PricingByDistanceTest {

    @DisplayName("모든 전략을 리스트로 준다.")
    @Test
    void getAllStrategy() {
        List<PricingByDistance> strategies = PricingByDistance.getAllStrategies();

        assertThat(strategies.size()).isEqualTo(3);
        assertThat(strategies.contains(PricingOverFifty.of())).isTrue();
        assertThat(strategies.contains(PricingTenToFifty.of())).isTrue();
        assertThat(strategies.contains(PricingUnderTen.of())).isTrue();
    }

}
