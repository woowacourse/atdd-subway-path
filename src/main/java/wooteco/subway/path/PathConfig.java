package wooteco.subway.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Configuration
public class PathConfig {
    private final SectionDao sectionDao;

    @Autowired
    public PathConfig(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Bean
    public WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Section> sections = sectionDao.findAll();

        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
        return graph;
    }
}
