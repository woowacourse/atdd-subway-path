package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.admin.dto.GraphResultResponse;

@Component
public class SubwayGraph implements TranslationGraph {
    public SubwayGraph() {
    }

    @Override
    public GraphResultResponse findShortestPath(List<Line> lines, Long source, Long target, CriteriaType type) {
        WeightedMultigraph<Long, CustomEdge> graph = generateWeightedMultiGraph(lines, type);
        final GraphPath<Long, CustomEdge> shortestPath = getShortestPath(graph, source, target);

        validateNoConnection(shortestPath);
        return getPathLineStation(shortestPath.getEdgeList(), shortestPath.getVertexList());
    }

    private WeightedMultigraph<Long, CustomEdge> generateWeightedMultiGraph(List<Line> lines, CriteriaType type) {
        WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);

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

        return graph;
    }

    private GraphPath<Long, CustomEdge> getShortestPath(WeightedMultigraph<Long, CustomEdge> graph, Long source,
        Long target) {
        DijkstraShortestPath<Long, CustomEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(source, target);
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

    private void validateNoConnection(GraphPath<Long, CustomEdge> path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }
}
