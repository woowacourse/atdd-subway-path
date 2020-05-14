package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;

public class Path {
    private final GraphPath<Station, StationWeightEdge> path;

    public Path(List<Line> lines, List<Station> stations, Station source, Station target) {
        WeightedMultigraph<Station, StationWeightEdge> graph = createGraph(lines, stations);
        this.path = new DijkstraShortestPath<>(graph).getPath(source, target);
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
