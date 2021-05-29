package wooteco.subway.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.infrastructure.ShortestPathWithDijkstra;
import wooteco.subway.station.domain.Station;

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
        return new ShortestPathWithDijkstra(weightedMultigraph(), new Sections(sections));
    }

    @Bean
    public WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph() {
        return new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }
}
