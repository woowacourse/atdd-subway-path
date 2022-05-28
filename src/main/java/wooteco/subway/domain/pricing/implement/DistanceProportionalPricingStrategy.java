package wooteco.subway.domain.pricing.implement;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.pricing.PricingStrategy;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public class DistanceProportionalPricingStrategy implements PricingStrategy {

    private final PricingBySection pricingBySection;

    public DistanceProportionalPricingStrategy(
            @Qualifier("AllPricingBySectionStrategy") PricingBySection pricingBySection) {
        this.pricingBySection = pricingBySection;
    }

    @Override
    public Fare calculateFare(FareCacluateSpecification specification) {
        int distance = calculateDistance(specification.getSections());
        return pricingBySection.calculateFare(distance);
    }

    private int calculateDistance(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }
}
