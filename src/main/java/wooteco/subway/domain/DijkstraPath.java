package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPath implements Path {

    private final List<Section> sections;

    public DijkstraPath(final List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public List<Long> getShortestPathStationIds(Long departureId, Long arrivalId) {
        List<String> stationIds = findShortestPath(departureId, arrivalId).getVertexList();
        return stationIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    private GraphPath findShortestPath(Long departureId, Long arrivalId) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addStationVertex(graph);
        addSectionEdge(graph);
        GraphPath path = getGraphPath(departureId, arrivalId, graph);
        validateConnection(path);
        return path;
    }

    private void validateConnection(GraphPath path) {
        if (path == null) {
            throw new IllegalArgumentException("연결되지 않은 구간입니다.");
        }
    }

    private GraphPath getGraphPath(Long departureId, Long arrivalId,
                                   WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        org.jgrapht.alg.shortestpath.DijkstraShortestPath dijkstraShortestPath = new org.jgrapht.alg.shortestpath.DijkstraShortestPath(
                graph);
        try {
            return dijkstraShortestPath.getPath(String.valueOf(departureId), String.valueOf(arrivalId));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 등록 되지 않은 역입니다.");
        }
    }

    private void addStationVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        for (Long stationId : getAllStationIds()) {
            graph.addVertex(String.valueOf(stationId));
        }
    }

    private List<Long> getAllStationIds() {
        Set<Long> stationIds = new HashSet<>();

        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }

        return new ArrayList<>(stationIds);
    }

    private void addSectionEdge(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            String upStationId = String.valueOf(section.getUpStationId());
            String downStationId = String.valueOf(section.getDownStationId());
            int distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), distance);
        }
    }

    @Override
    public int getShortestPathDistance(Long departureId, Long arrivalId) {
        return (int) findShortestPath(departureId, arrivalId).getWeight();
    }
}
