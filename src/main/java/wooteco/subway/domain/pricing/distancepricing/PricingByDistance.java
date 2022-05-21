package wooteco.subway.domain.pricing.distancepricing;

import java.util.List;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingOverFifty;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingTenToFifty;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingUnderTen;

public abstract class PricingByDistance {

    public abstract int calculateFee(int distance);

    public static List<PricingByDistance> getAllStrategies() {
        return List.of(PricingOverFifty.of(), PricingTenToFifty.of(), PricingUnderTen.of());
    }
}
