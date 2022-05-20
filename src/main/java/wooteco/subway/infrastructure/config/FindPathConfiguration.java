package wooteco.subway.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.path.FindPathStrategy;
import wooteco.subway.infrastructure.path.FindDijkstraShortestPathStrategy;

@Configuration
public class FindPathConfiguration {

    @Bean
    public FindPathStrategy findPathStrategy() {
        return new FindDijkstraShortestPathStrategy();
    }
}
