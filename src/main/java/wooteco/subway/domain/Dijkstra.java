package wooteco.subway.domain;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Dijkstra implements PathFactory {

    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public Dijkstra(Sections sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initVertexes(graph, sections);
        addEdgeWeights(graph, sections);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void initVertexes(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Sections sections) {
        for (Long stationId : sections.getStationIdsInSections()) {
            graph.addVertex(stationId);
        }
    }

    private void addEdgeWeights(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistance());
        }
    }

    public int findShortestDistance(Long source, Long target) {
        double weight = dijkstraShortestPath.getPath(source, target).getWeight();
        return (int) weight;
    }

    public List<Long> findShortestPath(Long source, Long target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
