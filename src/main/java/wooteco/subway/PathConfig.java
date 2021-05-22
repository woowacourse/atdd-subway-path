package wooteco.subway;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.station.domain.Station;

@Configuration
public class PathConfig {
    @Bean
    public AbstractBaseGraph<Station, DefaultWeightedEdge> graph() {
        return new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    @Bean
    public ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm() {
        return new DijkstraShortestPath<>(graph());
    }
}
