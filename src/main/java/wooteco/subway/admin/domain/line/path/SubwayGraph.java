package wooteco.subway.admin.domain.line.path;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.domain.line.LineStation;

import java.util.Set;

public class SubwayGraph {
    private final WeightedGraph<Long, RouteEdge> graph;

    private SubwayGraph(WeightedGraph<Long, RouteEdge> graph) {
        this.graph = graph;
    }

    public static SubwayGraph of(Set<LineStation> lineStations, EdgeWeightStrategy edgeWeightStrategy) {
        WeightedGraph<Long, RouteEdge> graph = new WeightedMultigraph<>(RouteEdge.class);
        for (LineStation lineStation : lineStations) {
            setEdge(edgeWeightStrategy, graph, lineStation);
        }
        return new SubwayGraph(graph);
    }

    private static void setEdge(EdgeWeightStrategy edgeWeightStrategy, WeightedGraph<Long, RouteEdge> graph, LineStation lineStation) {
        RouteEdge routeEdge = lineStation.toEdge();
        graph.addVertex(lineStation.getStationId());
        if (lineStation.isNotStart()) {
            graph.addVertex(lineStation.getPreStationId());
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), routeEdge);
            edgeWeightStrategy.setWeight(graph, routeEdge);
        }
    }

    public SubwayRoute findDijkstraShortestPath(Long departureId, Long arrivalId) {
        return new SubwayRoute(DijkstraShortestPath.findPathBetween(graph, departureId, arrivalId));
    }
}
