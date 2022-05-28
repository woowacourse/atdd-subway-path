package wooteco.subway.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import wooteco.subway.domain.pricing.PricingStrategy;
import wooteco.subway.domain.pricing.implement.AllPricingStrategy;
import wooteco.subway.domain.pricing.implement.DistanceProportionalPricingStrategy;
import wooteco.subway.domain.pricing.implement.LineAdditionalPricingStrategy;

@Configuration
@Import(PricingBySectionConfig.class)
public class PricingStrategyConfig {

    @Autowired
    PricingBySectionConfig pricingBySectionConfig;

    @Bean
    @Qualifier("DistanceProportional")
    public PricingStrategy distanceProportionalPricingStrategy() {
        return new DistanceProportionalPricingStrategy(pricingBySectionConfig.allPricingBySectionStrategy());
    }

    @Bean
    @Qualifier("LineAdditional")
    public PricingStrategy lineAdditionalPricingStrategy() {
        return new LineAdditionalPricingStrategy();
    }

    @Bean
    @Qualifier("AllPricingStrategy")
    public PricingStrategy allPricingStrategy() {
        return new AllPricingStrategy(pricingStrategies());
    }

    @Bean
    public List<PricingStrategy> pricingStrategies() {
        return List.of(distanceProportionalPricingStrategy(), lineAdditionalPricingStrategy());
    }
}
