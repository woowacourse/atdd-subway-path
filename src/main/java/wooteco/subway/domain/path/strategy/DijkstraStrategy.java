package wooteco.subway.domain.path.strategy;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import wooteco.subway.domain.path.PathEdge;
import wooteco.subway.domain.station.Station;

public class DijkstraStrategy implements PathFindingStrategy {
    public GraphPath<Station, PathEdge> findPathBetween(Graph<Station, PathEdge> graph, Station source, Station target) {
        return DijkstraShortestPath.findPathBetween(graph, source, target);
    }
}
