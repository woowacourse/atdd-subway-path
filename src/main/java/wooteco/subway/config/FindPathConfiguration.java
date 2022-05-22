package wooteco.subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.path.FindDijkstraShortestPathStrategy;
import wooteco.subway.domain.path.FindPathStrategy;

@Configuration
public class FindPathConfiguration {

    @Bean
    public FindPathStrategy findPathStrategy() {
        return new FindDijkstraShortestPathStrategy();
    }
}
