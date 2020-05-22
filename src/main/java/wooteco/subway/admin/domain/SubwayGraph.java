package wooteco.subway.admin.domain;

import java.util.List;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.dto.GraphResultResponse;

public class SubwayGraph {
    WeightedSubwayGraph weightedSubwayGraph;

    public SubwayGraph(List<Line> lines, CriteriaType type) {
        weightedSubwayGraph = new WeightedSubwayGraph(lines, type);
    }

    public GraphResultResponse findShortestPath(Long source, Long target) {
        DijkstraSubwayShortestPath dijkstraSubwayShortestPath = weightedSubwayGraph.generateDijkstra();

        final GraphPath<Long, CustomEdge> shortestPath = dijkstraSubwayShortestPath.getShortestPath(source, target);
        return getPathLineStation(shortestPath.getEdgeList(), shortestPath.getVertexList());
    }

    private GraphResultResponse getPathLineStation(List<CustomEdge> result, List<Long> stationIds) {
        int distance = result.stream()
            .map(CustomEdge::getLineStation)
            .mapToInt(LineStation::getDistance)
            .sum();

        int duration = result.stream()
            .map(CustomEdge::getLineStation)
            .mapToInt(LineStation::getDuration)
            .sum();
        return new GraphResultResponse(stationIds, distance, duration);
    }

}
