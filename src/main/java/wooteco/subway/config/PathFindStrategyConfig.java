package wooteco.subway.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.path.PathFindStrategy;
import wooteco.subway.domain.path.implement.MinimumDistanceFindStrategy;

@Configuration
public class PathFindStrategyConfig {

    @Bean
    @Qualifier("MinimumDistance")
    public PathFindStrategy minimumDistanceFindStrategy() {
        return new MinimumDistanceFindStrategy();
    }
}
