package wooteco.subway.domain.pricing.distancepricing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInThirdSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInSecondSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInFirstSection;

class PricingBySectionTest {

    @DisplayName("모든 전략을 리스트로 준다.")
    @Test
    void getAllStrategy() {
        List<PricingBySection> strategies = PricingBySection.getAllStrategies();

        assertThat(strategies.size()).isEqualTo(3);
        assertThat(strategies.contains(PricingInThirdSection.of())).isTrue();
        assertThat(strategies.contains(PricingInSecondSection.of())).isTrue();
        assertThat(strategies.contains(PricingInFirstSection.of())).isTrue();
    }

}
