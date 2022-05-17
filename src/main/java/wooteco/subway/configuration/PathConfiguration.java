package wooteco.subway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.strategy.FindDijkstraShortestPathStrategy;
import wooteco.subway.domain.strategy.FindPathStrategy;

@Configuration
public class PathConfiguration {

    @Bean
    public FindPathStrategy findPathStrategy() {
        return new FindDijkstraShortestPathStrategy();
    }
}
