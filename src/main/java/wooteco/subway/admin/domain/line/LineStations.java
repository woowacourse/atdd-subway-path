package wooteco.subway.admin.domain.line;

import static java.util.stream.Collectors.*;

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
}
