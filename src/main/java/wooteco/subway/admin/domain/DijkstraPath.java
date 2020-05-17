package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.InvalidSubwayPathException;
import wooteco.subway.admin.exception.StationNotFoundException;

public class DijkstraPath implements PathStrategy {

    private final List<Line> lines;
    private final List<Station> stations;
    private final Station source;
    private final Station target;

    public DijkstraPath(List<Line> lines, List<Station> stations, final String source, final String target) {
        this.lines = lines;
        this.stations = stations;
        this.source = stations.stream().filter(station -> station.getName().equals(source)).findAny()
            .orElseThrow(() -> new StationNotFoundException(source));
        this.target = stations.stream().filter(station -> station.getName().equals(target)).findAny()
            .orElseThrow(() -> new StationNotFoundException(target));
    }

    @Override
    public PathResponse getPath(PathType type) {
        validateSourceTargetEquality(source, target);

        WeightedMultigraph<Long, PathEdge> graph = initializeGraph();
        addEdgeByPathType(type, graph);
        GraphPath<Long, PathEdge> path = DijkstraShortestPath.findPathBetween(graph, source.getId(), target.getId());

        List<StationResponse> stations = findStationsFromPath(path);
        return new PathResponse(stations, getDistance(path), getDuration(path));
    }

    private void validateSourceTargetEquality(final Station source, final Station target) {
        if (source.is(target)) {
            throw new InvalidSubwayPathException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private WeightedMultigraph<Long, PathEdge> initializeGraph() {
        WeightedMultigraph<Long, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        stations.stream()
            .map(Station::getId)
            .forEach(graph::addVertex);
        return graph;
    }

    private void addEdgeByPathType(final PathType pathType, final WeightedMultigraph<Long, PathEdge> graph) {
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

    private List<StationResponse> findStationsFromPath(final GraphPath<Long, PathEdge> path) {
        validatePath(path);
        return path.getVertexList().stream()
            .map(this::getStation)
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    private void validatePath(final GraphPath<Long, PathEdge> path) {
        if (Objects.isNull(path)) {
            throw new InvalidSubwayPathException("존재하지 않는 경로입니다.");
        }
    }

    private Station getStation(final Long id) {
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
