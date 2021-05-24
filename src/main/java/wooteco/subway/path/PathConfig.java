package wooteco.subway.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class PathConfig {
    private final SectionDao sectionDao;
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathConfig(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Bean
    public WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph() {
        this.graph =  new WeightedMultigraph<>(DefaultWeightedEdge.class);
        return this.graph;
    }

    @PostConstruct
    public void init() {
        List<Section> sections = sectionDao.findAll();

        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
