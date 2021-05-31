package wooteco.subway.path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.infrastructure.ShortestPathWithDijkstra;

import java.util.List;

@Configuration
public class PathConfig {
    private final SectionDao sectionDao;

    public PathConfig(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Bean
    public ShortestPathWithDijkstra shortestPathWithDijkstra() {
        List<Section> sections = sectionDao.findAll();
        return new ShortestPathWithDijkstra(sections);
    }
}
