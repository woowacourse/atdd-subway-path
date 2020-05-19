package wooteco.subway.admin.domain;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class DijkstraShortestPathStrategy implements PathStrategy {
    @Override
    public GraphPath<Long, LineStationEdge> getPath(Long sourceId, Long targetId,
            Graph<Long, LineStationEdge> graph) {
        return DijkstraShortestPath.findPathBetween(graph, sourceId, targetId);
    }
}
