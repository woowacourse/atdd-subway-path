package wooteco.subway.admin.domain.line.path;

import java.util.Set;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.line.LineStation;

public class SubwayGraph implements SubwayMap {
    private final WeightedGraph<Long, RouteEdge> graph;

    private SubwayGraph(WeightedGraph<Long, RouteEdge> graph) {
        this.graph = graph;
    }

    public static SubwayMap of(Set<LineStation> lineStations, EdgeWeightStrategy edgeWeightStrategy) {
        WeightedGraph<Long, RouteEdge> graph = new WeightedMultigraph<>(RouteEdge.class);
        for (LineStation lineStation : lineStations) {
            setEdge(edgeWeightStrategy, graph, lineStation);
        }
        return new SubwayGraph(graph);
    }

    private static void setEdge(EdgeWeightStrategy edgeWeightStrategy, WeightedGraph<Long, RouteEdge> graph,
        LineStation lineStation) {
        RouteEdge routeEdge = lineStation.toEdge();
        graph.addVertex(lineStation.getStationId());
        if (lineStation.isNotStart()) {
            graph.addVertex(lineStation.getPreStationId());
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), routeEdge);
            graph.setEdgeWeight(routeEdge, edgeWeightStrategy.getWeight(routeEdge));
        }
    }

    @Override
    public Path findShortestPath(Long departureId, Long arrivalId) {
        return new SubwayRoute(DijkstraShortestPath.findPathBetween(graph, departureId, arrivalId));
    }
}
