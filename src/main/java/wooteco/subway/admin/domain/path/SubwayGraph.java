package wooteco.subway.admin.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph implements Graph {
    private WeightedMultigraph<Long, LineStationEdge> graph;

    private SubwayGraph(WeightedMultigraph graph) {
        this.graph = graph;
    }

    public static SubwayGraph of(WeightedMultigraph graph) {
        return new SubwayGraph(graph);
    }

    @Override
    public SubwayPath findPath(Long sourceId, Long targetId) {
        GraphPath<Long, LineStationEdge> path = DijkstraShortestPath.findPathBetween(graph,
            sourceId, targetId);
        return new SubwayPath(path.getVertexList(), path.getEdgeList());
    }
}
