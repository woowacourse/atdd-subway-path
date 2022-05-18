package wooteco.subway.domain.path;


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public interface PathFindingStrategy {

    int calculateShortestDistance(Graph<Long, DefaultWeightedEdge> graph, Long source, Long target);

    List<Long> getShortestPath(Graph<Long, DefaultWeightedEdge> graph, Long source, Long target);
}
