package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.SourceTargetSameException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(String sourceName, String targetName, String type) {
        validate(sourceName, targetName, type);

        Station source = stationService.findByName(sourceName);
        Station target = stationService.findByName(targetName);
        List<Line> lines = lineService.findLines();

        Map<Long, Station> stations = stationService.findAll()
                .stream()
                .collect(toMap(Station::getId, Function.identity()));

        PathType pathType = PathType.of(type);

        DijkstraShortestPath<Station, LineStationEdge> path = shortestPath(lines, stations, pathType.getStrategy());
        GraphPath<Station, LineStationEdge> graphPath = path.getPath(source, target);
        validatePath(graphPath, sourceName, targetName);

        List<Station> shortestPath = graphPath.getVertexList();

        int shortestDistance = pathType.calculateDistance(graphPath);
        int shortestDuration = pathType.calculateDuration(graphPath);

        return new PathResponse(StationResponse.listOf(shortestPath), shortestDistance, shortestDuration);
    }

    private void validate(String sourceName, String targetName, String Type) {
        if (Objects.equals(targetName, sourceName)) {
            throw new SourceTargetSameException(sourceName);
        }
    }

    private void validatePath(GraphPath<Station, LineStationEdge> graphPath, String sourceName, String targetName) {
        if (Objects.isNull(graphPath)) {
            throw new NotFoundPathException(sourceName, targetName);
        }
    }

    private DijkstraShortestPath<Station, LineStationEdge> shortestPath(List<Line> lines, Map<Long, Station> stations, Function<LineStation, Integer> weightStrategy) {
        WeightedMultigraph<Station, LineStationEdge> graph = new WeightedMultigraph<>(LineStationEdge.class);

        List<LineStation> lineStations = linesToLineStations(lines);

        stations.values()
                .forEach(graph::addVertex);

        lineStations.forEach(lineStation -> {
            graph.addEdge(stations.get(lineStation.getPreStationId()),
                    stations.get(lineStation.getStationId()),
                    new LineStationEdge(lineStation, weightStrategy));
        });

        return new DijkstraShortestPath<>(graph);
    }

    private List<LineStation> linesToLineStations(List<Line> lines) {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Set::stream)
                .filter(LineStation::isNotStarting)
                .collect(Collectors.toList());
    }
}