package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.exception.StationNotFoundException;

public class Subway {
    private final List<Line> lines;
    private final List<Station> stations;

    public Subway(List<Line> lines, List<Station> stations) {
        validateLines(lines);
        validateStations(stations);
        this.lines = lines;
        this.stations = stations;
    }

    private void validateLines(List<Line> lines) {
        if (Objects.isNull(lines)) {
            throw new LineNotFoundException();
        }
    }

    private void validateStations(List<Station> stations) {
        if (Objects.isNull(stations)) {
            throw new StationNotFoundException();
        }
    }

    public ShortestPath findShortestPath(String source, String target, PathType pathType) {
        Map<Long, Station> stationMapper = generateStationMapper();

        WeightedMultigraph<Station, SubwayEdge> subwayGraph = new WeightedMultigraph<>(
                SubwayEdge.class);
        addVertices(subwayGraph);
        addEdges(subwayGraph, stationMapper, pathType);

        DijkstraShortestPath<Station, SubwayEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                subwayGraph);
        Station sourceId = findStationByName(source);
        Station targetId = findStationByName(target);

        return new ShortestPath(dijkstraShortestPath.getPath(sourceId, targetId));
    }

    private Station findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.isSameName(stationName))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(stationName));
    }

    private void addVertices(WeightedMultigraph<Station, SubwayEdge> subwayGraph) {
        for (Station station : stations) {
            subwayGraph.addVertex(station);
        }
    }

    private void addEdges(WeightedMultigraph<Station, SubwayEdge> subwayGraph, Map<Long, Station> stationMapper,
            PathType pathType) {
        List<LineStation> lineStations = generateLineStations();
        for (LineStation lineStation : lineStations) {
            SubwayEdge edge = new SubwayEdge(lineStation, pathType);
            subwayGraph.addEdge(stationMapper.get(lineStation.getPreStationId()),
                    stationMapper.get(lineStation.getStationId()), edge);
        }
    }

    private Map<Long, Station> generateStationMapper() {
        return stations.stream()
                .collect(Collectors.toMap(
                        Station::getId,
                        station -> station));
    }

    private List<LineStation> generateLineStations() {
        return lines.stream()
                .flatMap(line -> line.getLineStations().stream())
                .filter(LineStation::hasPreStation)
                .collect(Collectors.toList());
    }
}
