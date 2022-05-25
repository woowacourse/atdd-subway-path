package wooteco.subway.domain.pricing.distancepricing.implement;

import java.util.List;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public class AllPricingBySectionStrategy implements PricingBySection {

    private final List<PricingBySection> pricingBySections;

    public AllPricingBySectionStrategy(List<PricingBySection> pricingBySections) {
        this.pricingBySections = pricingBySections;
    }

    @Override
    public int calculateFee(int distance) {
        return pricingBySections.stream()
                .mapToInt(it -> it.calculateFee(distance))
                .sum();
    }
}
