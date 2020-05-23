package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.dto.GraphResultResponse;

public class SubwayGraph implements translationGraph {
    WeightedMultigraph<Long, CustomEdge> graph;

    public SubwayGraph(List<Line> lines, CriteriaType type) {
        this.graph = new WeightedMultigraph<>(CustomEdge.class);

        lines.stream()
            .flatMap(it -> it.getLineStationsId().stream())
            .forEach(graph::addVertex);

        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .filter(it -> Objects.nonNull(it.getPreStationId()))
            .forEach(
                it -> graph.addEdge(it.getPreStationId(), it.getStationId(),
                    new CustomEdge(it, type)
                ));
    }

    @Override
    public GraphResultResponse findShortestPath(Long source, Long target) {
        final GraphPath<Long, CustomEdge> shortestPath = getShortestPath(source, target);

        validateNoConnection(shortestPath);
        return getPathLineStation(shortestPath.getEdgeList(), shortestPath.getVertexList());
    }

    private GraphPath<Long, CustomEdge> getShortestPath(Long source, Long target) {
        return generateDijkstraShortestPath().getPath(source, target);
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

    private DijkstraShortestPath<Long, CustomEdge> generateDijkstraShortestPath() {
        return new DijkstraShortestPath<>(this.graph);
    }

    private void validateNoConnection(GraphPath<Long, CustomEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }
}
