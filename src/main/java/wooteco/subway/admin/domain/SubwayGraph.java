package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {
    private WeightedMultigraph<Station, LineStationEdge> graph;

    public SubwayGraph(List<Line> lines, Map<Long, Station> stations, Function<LineStation, Integer> weightStrategy) {
        this.graph = initialize(lines, stations, weightStrategy);
    }

    private WeightedMultigraph<Station, LineStationEdge> initialize(List<Line> lines, Map<Long, Station> stations, Function<LineStation, Integer> weightStrategy) {
        WeightedMultigraph<Station, LineStationEdge> graph = new WeightedMultigraph<>(LineStationEdge.class);

        List<LineStation> lineStations = linesToLineStations(lines);

        stations.values()
                .forEach(graph::addVertex);

        lineStations.forEach(lineStation -> {
            graph.addEdge(stations.get(lineStation.getPreStationId()),
                    stations.get(lineStation.getStationId()),
                    new LineStationEdge(lineStation, weightStrategy));
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
        return new SubWayPath(new DijkstraShortestPath<>(graph), source, target);
    }
}
