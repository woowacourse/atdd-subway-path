package wooteco.subway.domain.pricing.distancepricing.implement;

import java.util.List;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public class AllPricingBySectionStrategy implements PricingBySection {

    private final List<PricingBySection> pricingBySections;

    public AllPricingBySectionStrategy(List<PricingBySection> pricingBySections) {
        this.pricingBySections = pricingBySections;
    }

    @Override
    public Fare calculateFare(int distance) {
        return pricingBySections.stream()
                .map(it -> it.calculateFare(distance))
                .reduce(new Fare(0), Fare::sum);
    }
}
