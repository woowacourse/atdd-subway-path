package wooteco.subway.domain.pricing.implement;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.pricing.PricingStrategy;

@Component
@Qualifier("All")
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
