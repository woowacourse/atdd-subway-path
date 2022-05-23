package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathCalculator {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public PathCalculator(final List<Section> sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        for (Section section : sections) {
            addSectionInGraph(graph, section);
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addSectionInGraph(final WeightedMultigraph<Long, DefaultWeightedEdge> graph,
                                   final Section section) {
        final Long upStationId = section.getUpStationId();
        final Long downStationId = section.getDownStationId();

        graph.addVertex(upStationId);
        graph.addVertex(downStationId);
        graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), section.getDistance());
        graph.setEdgeWeight(graph.addEdge(downStationId, upStationId), section.getDistance());
    }

    public List<Long> findShortestPath(final Long source, final Long target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double findShortestDistance(final Long source, final Long target) {
        return dijkstraShortestPath.getPath(source, target).getWeight();
    }
}
