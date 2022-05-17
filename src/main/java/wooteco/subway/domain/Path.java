package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public Path(Sections sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addVertex(sections);
        addEdge(sections);
    }

    public List<Long> createShortestPath(Long upStationId, Long downStationId) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<Long> shortestPath = dijkstraShortestPath.getPath(upStationId,
                downStationId).getVertexList();

        return shortestPath;
    }

    public int calculateDistance(Long upStationId, Long downStationId) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return (int)(dijkstraShortestPath.getPath(upStationId, downStationId).getWeight());
    }

    private void addVertex(Sections sections) {
        for (Station station : sections.getStations()) {
            graph.addVertex(station.getId());
        }
    }

    private void addEdge(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(),
                    section.getDownStation().getId()), section.getDistance());
        }
    }
}
