package wooteco.subway.admin.domain.path;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.LineStation;

public class SubwayGraphStrategy<V, E> implements GraphStrategy {
    private WeightedMultigraph<Long, LineStationEdge> graph;

    @Override
    public void makeGraph(List<Long> vertexList, List<LineStation> edgeList, PathType pathType) {
        graph = new WeightedMultigraph<>(LineStationEdge.class);
        vertexList.forEach(graph::addVertex);
        edgeList.forEach(edge -> makeEdge(edge, pathType));
    }

    @Override
    public Path findPath(Long sourceId, Long targetId) {
        GraphPath<Long, LineStationEdge> path = DijkstraShortestPath.findPathBetween(graph,
            sourceId, targetId);
        return new Path(path.getVertexList(), path.getEdgeList());
    }

    private void makeEdge(LineStation lineStation, PathType pathType) {
        if (lineStation.notFirstStation()) {
            LineStationEdge edge = LineStationEdge.of(lineStation);
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), edge);
            graph.setEdgeWeight(edge, pathType.getWeight(lineStation));
        }
    }
}
