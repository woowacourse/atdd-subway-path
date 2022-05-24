package wooteco.subway.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.pricing.PricingStrategy;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInFirstSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInSecondSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInThirdSection;
import wooteco.subway.domain.pricing.implement.DistanceProportionalPricingStrategy;
import wooteco.subway.domain.pricing.implement.LineAdditionalPricingStrategy;

@Configuration
public class SpringConfig {

    @Bean
    public PricingBySection pricingInFirstSection() {
        return new PricingInFirstSection();
    }

    @Bean
    public PricingBySection pricingInSecondSection() {
        return new PricingInSecondSection();
    }

    @Bean
    public PricingBySection pricingInThirdSection() {
        return new PricingInThirdSection();
    }

    @Bean
    public List<PricingBySection> pricingBySections() {
        return List.of(pricingInFirstSection(), pricingInSecondSection(), pricingInThirdSection());
    }

    @Bean
    @Qualifier("DistanceProportional")
    public PricingStrategy distanceProportionalPricingStrategy() {
        return new DistanceProportionalPricingStrategy(pricingBySections());
    }

    @Bean
    @Qualifier("LineAdditional")
    public PricingStrategy lineAdditionalPricingStrategy() {
        return new LineAdditionalPricingStrategy();
    }

    @Bean
    public List<PricingStrategy> pricingStrategies() {
        return List.of(distanceProportionalPricingStrategy(), lineAdditionalPricingStrategy());
    }
}
