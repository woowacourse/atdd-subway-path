package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse generatePathResponse(String sourceName, String targetName) {
        Station source = stationService.findByName(sourceName);
        Station target = stationService.findByName(targetName);
        List<Line> lines = lineService.findLines();

        Map<Long, Station> stations = stationService.findAll()
                .stream()
                .collect(toMap(Station::getId, Function.identity()));

        DijkstraShortestPath path = shortestPath(lines, stations, LineStation::getDistance);
        GraphPath graphPath = path.getPath(source, target);

        List<Station> shortestPath = graphPath.getVertexList();
        int shortestDistance = (int)graphPath.getWeight();
        int shortestDuration = calculateCost(shortestPath, lines, LineStation::getDuration);

        return new PathResponse(StationResponse.listOf(shortestPath), shortestDistance, shortestDuration);
    }

    private DijkstraShortestPath shortestPath(List<Line> lines, Map<Long, Station> stations, Function<LineStation, Integer> weightStrategy) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<LineStation> lineStations = linesToLineStations(lines);

        stations.values()
                .forEach(graph::addVertex);

        lineStations.forEach(lineStation ->
                graph.setEdgeWeight(graph.addEdge(stations.get(lineStation.getPreStationId()),
                        stations.get(lineStation.getStationId())),
                        weightStrategy.apply(lineStation)));

        return new DijkstraShortestPath(graph);
    }

    private List<LineStation> linesToLineStations(List<Line> lines) {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Set::stream)
                .filter(LineStation::isNotStarting)
                .collect(Collectors.toList());
    }

    private int calculateCost(List<Station> stations, List<Line> lines, Function<LineStation, Integer> costStrategy) {
        List<LineStation> lineStations = linesToLineStations(lines);

        return IntStream.range(1, stations.size())
                .mapToObj(i -> findLineStation(lineStations, stations.get(i - 1), stations.get(i)))
                .mapToInt(costStrategy::apply)
                .sum();
    }

    private LineStation findLineStation(List<LineStation> lineStations, Station preStation, Station station) {
        return lineStations.stream()
                .filter(lineStation -> lineStation.hasPreStationId(preStation.getId())
                        && lineStation.hasStationId(station.getId()))
                .findAny()
                .orElseThrow(() -> new NotFoundLineStationException(preStation.getName(), station.getName()));
    }
}