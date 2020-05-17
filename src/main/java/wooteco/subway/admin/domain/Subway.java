package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
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

    public ShortestPath findShortestPath(String sourceName, String targetName, PathType pathType) {
        validateStationName(sourceName, targetName);
        Station source = findStationByName(sourceName);
        Station target = findStationByName(targetName);

        Graph<Station, Edge> subwayGraph = createGraph(pathType);
        DijkstraShortestPath<Station, Edge> dijkstraShortestPath = new DijkstraShortestPath<>(subwayGraph);

        return new ShortestPath(dijkstraShortestPath.getPath(source, target));
    }

    private void validateStationName(String sourceName, String targetName) {
        if (sourceName.equals(targetName)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private Graph<Station, Edge> createGraph(PathType pathType) {
        Map<Long, Station> stationMapper = generateStationMapper();
        Graph<Station, Edge> subwayGraph = new WeightedMultigraph<>(Edge.class);
        addVertices(subwayGraph);
        addEdges(subwayGraph, stationMapper, pathType);
        return subwayGraph;
    }

    private Station findStationByName(String stationName) {
        return stations.stream()
                .filter(station -> station.isSameName(stationName))
                .findFirst()
                .orElseThrow(() -> new StationNotFoundException(stationName));
    }

    private void addVertices(Graph<Station, Edge> subwayGraph) {
        for (Station station : stations) {
            subwayGraph.addVertex(station);
        }
    }

    private void addEdges(Graph<Station, Edge> subwayGraph, Map<Long, Station> stationMapper, PathType pathType) {
        for (LineStation lineStation : generateLineStations()) {
            Station preStation = stationMapper.get(lineStation.getPreStationId());
            Station currentStation = stationMapper.get(lineStation.getStationId());
            Edge edge = new Edge(lineStation, pathType);
            subwayGraph.addEdge(preStation, currentStation, edge);
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
