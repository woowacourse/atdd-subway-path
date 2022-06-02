package wooteco.subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.path.PathDecisionStrategy;
import wooteco.subway.domain.path.PathDijkstraDecisionStrategy;

@Configuration
public class PathConfiguration {

    @Bean
    public PathDecisionStrategy addPathDecisionStrategyBean() {
        return new PathDijkstraDecisionStrategy();
    }
}
