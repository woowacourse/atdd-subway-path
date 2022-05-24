package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraPath implements Path {

    private final List<Section> sections;

    public DijkstraPath(final List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public List<Long> getShortestPathStationIds(Long departureId, Long arrivalId) {
        return findShortestPath(departureId, arrivalId).getVertexList();
    }

    private GraphPath findShortestPath(Long departureId, Long arrivalId) {
        WeightedMultigraph<Long, LineEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
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
                                   WeightedMultigraph<Long, LineEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(departureId, arrivalId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 등록 되지 않은 역입니다.");
        }
    }

    private void addStationVertex(WeightedMultigraph<Long, LineEdge> graph) {
        for (Long stationId : getAllStationIds()) {
            graph.addVertex(stationId);
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

    private void addSectionEdge(WeightedMultigraph<Long, LineEdge> graph) {
        for (Section section : sections) {
            int distance = section.getDistance();
            Long lineId = section.getLineId();
            graph.addEdge(section.getUpStationId(), section.getDownStationId(), new LineEdge(lineId, distance));
        }
    }

    @Override
    public int getShortestPathDistance(Long departureId, Long arrivalId) {
        return (int) findShortestPath(departureId, arrivalId).getWeight();
    }

    @Override
    public List<Long> getShortestPathLineIds(Long departureId, Long arrivalId) {
        List<LineEdge> edges = findShortestPath(departureId, arrivalId).getEdgeList();
        return edges.stream()
                .map(LineEdge::getLindId)
                .collect(Collectors.toList());
    }
}
