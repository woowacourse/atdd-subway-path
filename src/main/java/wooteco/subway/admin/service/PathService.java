package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundLineStationException;
import wooteco.subway.admin.exception.NotFoundStationException;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Service
public class PathService {
    private final LineService lineService;
    private final StationRepository stationRepository;

    public PathService(LineService lineService, StationRepository stationRepository) {
        this.lineService = lineService;
        this.stationRepository = stationRepository;
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> generateGraph(List<Line> lines, Map<Long, Station> stations) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<LineStation> lineStations = linesToLineStations(lines);

        for (Station station : stations.values()) {
            graph.addVertex(station);
        }

        for (LineStation lineStation : lineStations) {
            graph.setEdgeWeight(graph.addEdge(stations.get(lineStation.getPreStationId()), stations.get(lineStation.getStationId())), lineStation.getDistance());
        }

        return graph;
    }

    private List<Station> findPath(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private int calculateCost(List<Station> stations, List<Line> lines, Function<LineStation, Integer> costStrategy) {
        List<LineStation> lineStations = linesToLineStations(lines);

        return IntStream.range(1, stations.size())
                .mapToObj(i -> findLineStation(lineStations, stations.get(i - 1), stations.get(i)))
                .mapToInt(costStrategy::apply)
                .sum();
    }

    private List<LineStation> linesToLineStations(List<Line> lines) {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Set::stream)
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .collect(Collectors.toList());
    }

    private LineStation findLineStation(List<LineStation> lineStations, Station preStation, Station station) {
        return lineStations.stream()
                .filter(lineStation -> Objects.equals(lineStation.getPreStationId(), preStation.getId())
                        && Objects.equals(lineStation.getStationId(), station.getId()))
                .findAny()
                .orElseThrow(() -> new NotFoundLineStationException(preStation.getName(), station.getName()));
    }

    public PathResponse generatePathResponse(String sourceName, String targetName) {
        Station source = stationRepository.findByName(sourceName)
                .orElseThrow(() -> new NotFoundStationException(sourceName));
        Station target = stationRepository.findByName(targetName)
                .orElseThrow(() -> new NotFoundStationException(targetName));
        List<Line> lines = lineService.findLines();
        Map<Long, Station> stations = stationRepository.findAll()
                .stream()
                .collect(toMap(Station::getId, Function.identity()));
        List<Station> shortestPath = findPath(generateGraph(lines, stations), source, target);
        int shortestDistance = calculateCost(shortestPath, lines, LineStation::getDistance);
        int shortestDuration = calculateCost(shortestPath, lines, LineStation::getDuration);

        return new PathResponse(StationResponse.listOf(shortestPath), shortestDistance, shortestDuration);
    }
}