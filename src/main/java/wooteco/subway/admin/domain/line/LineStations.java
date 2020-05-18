package wooteco.subway.admin.domain.line;

import static java.util.stream.Collectors.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.line.path.EdgeWeightStrategy;
import wooteco.subway.admin.domain.line.path.RouteEdge;
import wooteco.subway.admin.domain.line.path.SubwayRoute;

public class LineStations {
    private final Set<LineStation> lineStations;

    public LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public SubwayRoute findShortestPath(EdgeWeightStrategy edgeWeightStrategy, Long departureId, Long arrivalId) {
        WeightedGraph<Long, RouteEdge> graph = new WeightedMultigraph<>(RouteEdge.class);
        Graphs.addAllVertices(graph, getStationIds());

        for (LineStation lineStation : lineStations) {
            lineStation.addEdgeTo(graph, edgeWeightStrategy);
        }

        return new SubwayRoute(DijkstraShortestPath.findPathBetween(graph, departureId, arrivalId));
    }

    private Set<Long> getStationIds() {
        return lineStations.stream()
            .map(LineStation::getStationId)
            .collect(toSet());
    }

    public Set<LineStation> getLineStations() {
        return lineStations;
    }

    public void add(LineStation lineStation) {
        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), lineStation.getPreStationId()))
            .findAny()
            .ifPresent(it -> it.updatePreLineStation(lineStation.getStationId()));

        lineStations.add(lineStation);
    }

    public void remove(Long stationId) {
        LineStation targetLineStation = findById(stationId);

        lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), stationId))
            .findFirst()
            .ifPresent(it -> it.updatePreLineStation(targetLineStation.getPreStationId()));

        lineStations.remove(targetLineStation);
    }

    private LineStation findById(Long stationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getStationId(), stationId))
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public boolean isEmpty() {
        return lineStations.isEmpty();
    }

    public LineStation getFirst() {
        return lineStations.stream()
            .filter(it -> it.getPreStationId() == null)
            .findFirst()
            .orElseThrow(RuntimeException::new);
    }

    public Optional<LineStation> getNextOf(Long stationId) {
        return lineStations.stream()
            .filter(it -> Objects.equals(it.getPreStationId(), stationId))
            .findFirst();
    }
}
