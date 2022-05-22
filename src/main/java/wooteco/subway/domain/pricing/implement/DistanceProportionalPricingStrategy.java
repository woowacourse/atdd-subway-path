package wooteco.subway.domain.pricing.implement;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.pricing.PricingStrategy;
import wooteco.subway.domain.pricing.distancepricing.PricingByDistance;

public class DistanceProportionalPricingStrategy implements PricingStrategy {

    @Override
    public int calculateFee(List<Section> sections) {
        int distance = calculateDistance(sections);
        List<PricingByDistance> pricingByDistances = PricingByDistance.getAllStrategies();
        return pricingByDistances.stream()
                .mapToInt(it -> it.calculateFee(distance))
                .sum();
    }

    private int calculateDistance(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }
}
