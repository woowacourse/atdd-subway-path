package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPath {

    private WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(
        DefaultWeightedEdge.class);

    public ShortestPath(Sections sections) {
        initVertexes(sections);
        addEdgeWeights(sections);
    }

    private void addEdgeWeights(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                section.getDistance());
        }
    }

    private void initVertexes(Sections sections) {
        for (Long stationId : sections.sortedStationId()) {
            graph.addVertex(stationId);
        }
    }

    public int findShortestDistance(Long source, Long target) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        double weight = dijkstraShortestPath.getPath(source, target).getWeight();
        return (int) weight;
    }

    public List<Long> findShortestPath(Long source, Long target) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
