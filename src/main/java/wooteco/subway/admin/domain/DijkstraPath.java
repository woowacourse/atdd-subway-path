package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.InvalidSubwayPathException;
import wooteco.subway.admin.exception.StationNotFoundException;

@Component
public class DijkstraPath implements PathStrategy {

    @Override
    public PathResponse getPath(List<Line> allLines, List<Station> allStations, PathRequest request) {
        final Station source = allStations.stream()
            .filter(station -> station.getName().equals(request.getSource()))
            .findAny()
            .orElseThrow(() -> new StationNotFoundException(request.getSource()));
        final Station target = allStations.stream()
            .filter(station -> station.getName().equals(request.getTarget()))
            .findAny()
            .orElseThrow(() -> new StationNotFoundException(request.getTarget()));

        validateSourceTargetEquality(source, target);

        WeightedMultigraph<Long, PathEdge> graph = initializeGraph(allStations);
        addEdgeByPathType(allLines, request.getType(), graph);
        GraphPath<Long, PathEdge> path = DijkstraShortestPath.findPathBetween(graph, source.getId(), target.getId());

        List<StationResponse> stations = findStationsFromPath(allStations, path);
        return new PathResponse(stations, getDistance(path), getDuration(path));
    }

    private void validateSourceTargetEquality(final Station source, final Station target) {
        if (source.is(target)) {
            throw new InvalidSubwayPathException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private WeightedMultigraph<Long, PathEdge> initializeGraph(List<Station> stations) {
        WeightedMultigraph<Long, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        stations.stream()
            .map(Station::getId)
            .forEach(graph::addVertex);
        return graph;
    }

    private void addEdgeByPathType(
        final List<Line> lines,
        final PathType pathType,
        final WeightedMultigraph<Long, PathEdge> graph
    ) {
        lines.stream()
            .map(Line::getLineStations)
            .flatMap(Collection::stream)
            .map(lineStation -> new PathEdge(lineStation, pathType))
            .filter(PathEdge::isNotFirst)
            .forEach(edge -> addEdge(graph, edge));
    }

    private void addEdge(final WeightedMultigraph<Long, PathEdge> graph, final PathEdge edge) {
        graph.addEdge(edge.getPreStationId(), edge.getStationId(), edge);
        graph.setEdgeWeight(edge, edge.getWeight());
    }

    private List<StationResponse> findStationsFromPath(
        final List<Station> stations,
        final GraphPath<Long, PathEdge> path
    ) {
        validatePath(path);
        return path.getVertexList().stream()
            .map(id -> getStation(stations, id))
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    private void validatePath(final GraphPath<Long, PathEdge> path) {
        if (Objects.isNull(path)) {
            throw new InvalidSubwayPathException("존재하지 않는 경로입니다.");
        }
    }

    private Station getStation(final List<Station> stations, final Long id) {
        return stations.stream()
            .filter(station -> station.is(id))
            .findAny()
            .orElseThrow(() -> new StationNotFoundException(id));
    }

    private Integer getDistance(final GraphPath<Long, PathEdge> path) {
        return path.getEdgeList()
            .stream()
            .map(PathEdge::getDistance)
            .reduce(Integer::sum)
            .orElse(0);
    }

    private Integer getDuration(final GraphPath<Long, PathEdge> path) {
        return path.getEdgeList()
            .stream()
            .map(PathEdge::getDuration)
            .reduce(Integer::sum)
            .orElse(0);
    }
}
