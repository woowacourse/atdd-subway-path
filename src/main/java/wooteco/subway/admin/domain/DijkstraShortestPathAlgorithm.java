package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class DijkstraShortestPathAlgorithm implements CustomShortestPathAlgorithm {
    @Override
    public GraphPath<Station, LineStationEdge> getPath(WeightedMultigraph<Station, LineStationEdge> graph,
                                                       Station source, Station target) {
        return new DijkstraShortestPath<>(graph).getPath(source, target);
    }
}
