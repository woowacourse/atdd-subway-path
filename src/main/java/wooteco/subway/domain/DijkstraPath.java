package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPath {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath;


    public DijkstraPath(Lines lines) {
        this.shortestPath = new DijkstraShortestPath<>(multiGraph(lines));
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> multiGraph(Lines lines) {

        WeightedMultigraph<Long, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.stations().stationIds().forEach(graph::addVertex);
        lines.allSections().forEach(section ->
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance())
        );

        return graph;
    }

    public List<Long> shortestPath(Long sourceStationId, Long targetStationId) {
        return shortestPath.getPath(sourceStationId, targetStationId).getVertexList();
    }

    public int distance(Long sourceStationId, Long targetStationId) {
        return (int) shortestPath.getPath(sourceStationId, targetStationId).getWeight();
    }
}
