package wooteco.subway.admin.domain.path;

import java.util.List;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.LineStation;

public class SubwayGraphStrategy implements GraphStrategy {
    private WeightedMultigraph<Long, LineStationEdge> graph;

    @Override
    public Graph makeGraph(List vertexList, List edgeList, PathType pathType, Class weightClass) {
        WeightedMultigraph<Long, LineStationEdge> graph = new WeightedMultigraph<Long, LineStationEdge>(
            weightClass);
        vertexList.forEach(vertex -> graph.addVertex((Long)vertex));
        edgeList.forEach(edge -> makeEdge((LineStation)edge, pathType));
        return SubwayGraph.of(graph);
    }

    private void makeEdge(LineStation lineStation, PathType pathType) {
        if (lineStation.notFirstStation()) {
            LineStationEdge edge = LineStationEdge.of(lineStation);
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), edge);
            graph.setEdgeWeight(edge, pathType.getWeight(lineStation));
        }
    }
}
