package wooteco.subway.config;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.path.domain.PathEdge;
import wooteco.subway.station.domain.Station;

@Configuration
public class ApplicationConfig {

    @Bean
    public Multigraph<Station, PathEdge> graph() {
        return new WeightedMultigraph<>(PathEdge.class);
    }

    @Bean
    public ShortestPathAlgorithm<Station, PathEdge> shortestPathAlgorithm() {
        return new DijkstraShortestPath<>(graph());
    }
}
