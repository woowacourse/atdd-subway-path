package wooteco.subway.admin.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Set;

public class SubwayPath {
    private WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public SubwayPath(Line line) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        Set<Edge> edges = line.getEdges();
        for (Edge edge : edges) {
            graph.addVertex(edge.getStationId());
        }
        for (Edge edge : edges) {
            graph.setEdgeWeight(graph.addEdge(edge.getPreStationId(), edge.getStationId()), edge.getDistance());
        }
        this.graph = graph;
    }


    public List<Long> getShortestPath(Long sourceStationId, Long targetStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(this.graph);
        return dijkstraShortestPath.getPath(sourceStationId, targetStationId)
                .getVertexList();
    }


}
