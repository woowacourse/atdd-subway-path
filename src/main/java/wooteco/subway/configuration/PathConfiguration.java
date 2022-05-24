package wooteco.subway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.domain.path.FindDijkstraShortestPathStrategy;
import wooteco.subway.domain.path.FindPathStrategy;

@Configuration
public class PathConfiguration {

    @Bean
    public FindPathStrategy findPathStrategy() {
        return new FindDijkstraShortestPathStrategy();
    }
}
