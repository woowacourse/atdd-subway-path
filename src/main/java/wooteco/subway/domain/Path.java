package wooteco.subway.domain;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph;

    public Path(List<Section> sections) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(sections);
        addEdge(sections);
    }

    public List<Long> createShortestPath(Long upStationId, Long downStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        try {
            return dijkstraShortestPath.getPath(upStationId, downStationId).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("최단 경로를 요청하신 역이 구간에 존재하지 않습니다.");
        }
    }

    public int calculateDistance(Long upStationId, Long downStationId) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return (int) (dijkstraShortestPath.getPath(upStationId, downStationId).getWeight());
    }

    private void addVertex(List<Section> sections) {
        Set<Station> stations = getStations(sections);
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    private Set<Station> getStations(List<Section> sections) {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        return stations;
    }

    private void addEdge(List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(),
                    section.getDownStation().getId()), section.getDistance());
        }
    }
}
