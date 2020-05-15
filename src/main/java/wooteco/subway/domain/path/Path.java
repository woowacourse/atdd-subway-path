package wooteco.subway.domain.path;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

public class Path {
    private final GraphPath<Station, StationWeightEdge> path;

    public Path(List<Line> lines, List<Station> stations, String sourceName, String targetName) {
        Station source = findStationByName(stations, sourceName);
        Station target = findStationByName(stations, targetName);

        WeightedMultigraph<Station, StationWeightEdge> graph = createGraph(lines, stations);
        GraphPath<Station, StationWeightEdge> path = new DijkstraShortestPath<>(graph).getPath(
            source, target);
        if (Objects.isNull(path)) {
            throw new InvalidPathException(sourceName, targetName);
        }
        this.path = path;
    }

    private WeightedMultigraph<Station, StationWeightEdge> createGraph(List<Line> lines,
        List<Station> stations) {
        WeightedMultigraph<Station, StationWeightEdge> graph
            = new WeightedMultigraph<>(StationWeightEdge.class);

        stations.forEach(graph::addVertex);

        List<LineStation> possibleEdges = createPossibleEdges(lines);

        possibleEdges.forEach(
            lineStation -> {
                Station preStation = searchStationById(stations, lineStation.getPreStationId());
                Station station = searchStationById(stations, lineStation.getStationId());

                StationWeightEdge edge = new StationWeightEdge(lineStation);
                graph.addEdge(preStation, station, edge);
                graph.setEdgeWeight(edge, edge.getDistance());
            });
        return graph;
    }

    private Station findStationByName(List<Station> stations, String source) {
        return stations.stream()
            .filter(station -> source.equals(station.getName()))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);
    }

    private List<LineStation> createPossibleEdges(List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getStations().stream())
            .filter(LineStation::isNotFirstStation)
            .collect(Collectors.toList());
    }

    private Station searchStationById(List<Station> stations, Long id) {
        return stations.stream()
            .filter(station -> id.equals(station.getId()))
            .findFirst()
            .orElseThrow(AssertionError::new);
    }

    public double duration() {
        return path.getEdgeList()
            .stream()
            .mapToDouble(StationWeightEdge::getDuration)
            .sum();
    }

    public List<Station> getVertexList() {
        return path.getVertexList();
    }

    public double getWeight() {
        return path.getWeight();
    }
}
