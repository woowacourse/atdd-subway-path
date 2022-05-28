package wooteco.subway.domain.pricing.implement;

import java.util.List;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.pricing.PricingStrategy;

public class AllPricingStrategy implements PricingStrategy {

    private List<PricingStrategy> pricingStrategies;

    public AllPricingStrategy(List<PricingStrategy> pricingStrategies) {
        this.pricingStrategies = pricingStrategies;
    }

    @Override
    public Fare calculateFare(FareCacluateSpecification specification) {
        return pricingStrategies.stream()
                .map(it -> it.calculateFare(specification))
                .reduce(new Fare(0), Fare::sum);
    }
}
