package wooteco.subway.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.discount.DiscountStrategy;
import wooteco.subway.domain.discount.implement.DiscountByAge;

@Configuration
public class DiscountStrategyConfig {

    @Bean
    @Qualifier("Age")
    public DiscountStrategy discountByAge() {
        return new DiscountByAge();
    }
}
