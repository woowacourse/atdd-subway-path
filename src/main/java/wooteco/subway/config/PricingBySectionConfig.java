package wooteco.subway.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;
import wooteco.subway.domain.pricing.distancepricing.implement.AllPricingBySectionStrategy;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInFirstSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInSecondSection;
import wooteco.subway.domain.pricing.distancepricing.implement.PricingInThirdSection;

@Configuration
public class PricingBySectionConfig {

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
    @Qualifier("AllPricingBySectionStrategy")
    public PricingBySection allPricingBySectionStrategy() {
        return new AllPricingBySectionStrategy(pricingBySections());
    }
}
