package wooteco.subway.domain.pricing.implement;

import java.util.List;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.pricing.PricingStrategy;

public class AllPricingStrategy implements PricingStrategy {

    private List<PricingStrategy> pricingStrategies;

    public AllPricingStrategy(List<PricingStrategy> pricingStrategies) {
        this.pricingStrategies = pricingStrategies;
    }

    @Override
    public int calculateFee(FareCacluateSpecification specification) {
        return pricingStrategies.stream()
                .mapToInt(it -> it.calculateFee(specification))
                .sum();
    }
}
