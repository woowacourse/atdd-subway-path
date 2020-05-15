package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class SubwayGraph {
    private WeightedMultigraph<Station, LineStationEdge> graph;
    private PathType pathType;

    public SubwayGraph(List<Line> lines, List<Station> stations, PathType pathType) {
        this.pathType = pathType;
        this.graph = initialize(lines, mapStations(stations));
    }

    private Map<Long, Station> mapStations(List<Station> stations) {
        return stations.stream()
                .collect(toMap(Station::getId, Function.identity()));
    }

    private WeightedMultigraph<Station, LineStationEdge> initialize(List<Line> lines, Map<Long, Station> stations) {
        WeightedMultigraph<Station, LineStationEdge> graph = new WeightedMultigraph<>(LineStationEdge.class);

        List<LineStation> lineStations = linesToLineStations(lines);

        stations.values()
                .forEach(graph::addVertex);

        lineStations.forEach(lineStation -> {
            graph.addEdge(stations.get(lineStation.getPreStationId()),
                    stations.get(lineStation.getStationId()),
                    new LineStationEdge(lineStation, pathType.getStrategy()));
        });

        return graph;
    }

    private List<LineStation> linesToLineStations(List<Line> lines) {
        return lines.stream()
                .map(Line::getStations)
                .flatMap(Set::stream)
                .filter(LineStation::isNotStarting)
                .collect(Collectors.toList());
    }

    public SubWayPath generatePath(Station source, Station target) {
        return new SubWayPath(new DijkstraShortestPath<>(graph), pathType, source, target);
    }
}
