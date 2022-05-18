package wooteco.subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.strategy.FindDijkstraShortestPathStrategy;
import wooteco.subway.domain.strategy.FindPathStrategy;

@Configuration
public class FindPathConfiguration {

    @Bean
    public FindPathStrategy findPathStrategy() {
        return new FindDijkstraShortestPathStrategy();
    }
}
