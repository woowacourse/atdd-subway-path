package wooteco.subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.fare.AgeDiscountPolicy;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.fare.FarePolicyImpl;

@Configuration
public class FareConfiguration {

    @Bean
    public FareCalculator fareCalculator() {
        return new FareCalculator(new FarePolicyImpl(), new AgeDiscountPolicy());
    }
}
