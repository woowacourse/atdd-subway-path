package wooteco.subway.admin.domain.path;

import java.util.List;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.LineStation;

public class SubwayGraphStrategy implements GraphStrategy<Long, LineStation> {

    @Override
    public Graph makeGraph(List<Long> vertexList, List<LineStation> edgeList, PathType pathType) {
        WeightedMultigraph<Long, LineStationEdge> graph = new WeightedMultigraph(
            LineStationEdge.class);
        vertexList.forEach(vertex -> graph.addVertex(vertex));
        edgeList.forEach(edge -> makeEdge(graph, edge, pathType));
        return SubwayGraph.of(graph);
    }

    private void makeEdge(WeightedMultigraph<Long, LineStationEdge> graph, LineStation lineStation,
        PathType pathType) {
        if (lineStation.notFirstStation()) {
            LineStationEdge edge = LineStationEdge.of(lineStation);
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), edge);
            graph.setEdgeWeight(edge, pathType.getWeight(lineStation));
        }
    }
}
