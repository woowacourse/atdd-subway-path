package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.CustomEdge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.dto.GraphResultResponse;

@Service
public class GraphService {
    public GraphResultResponse findPath(List<Line> lines, Long source, Long target,
        CriteriaType type) {
        WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);
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

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        final GraphPath path = dijkstraShortestPath.getPath(source, target);
        validateNoConnection(path);
        return getPathLineStation(path.getEdgeList(), path.getVertexList());
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

    private void validateNoConnection(GraphPath path) {
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }
}
