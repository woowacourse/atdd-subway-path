package wooteco.subway.domain.pricing.distancepricing;

import java.util.List;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInThirdSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInSecondSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInFirstSection;

public abstract class PricingBySection {

    public abstract int calculateFee(int distance);

    public static List<PricingBySection> getAllStrategies() {
        return List.of(PricingInThirdSection.of(), PricingInSecondSection.of(), PricingInFirstSection.of());
    }
}
