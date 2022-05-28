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
import org.springframework.stereotype.Component;

@Component
public class DijkstraPathFinder implements PathFinder {

    @Override
    public Path getShortestPath(List<Section> sections, long departureId, long arrivalId) {
        GraphPath graphPath = findShortestPath(sections, departureId, arrivalId);
        List<Long> stationIds = graphPath.getVertexList();
        int distance = (int) graphPath.getWeight();
        List<Long> lineIds = getShortestPathLineIds(graphPath);
        return new Path(stationIds, distance, lineIds);
    }

    private GraphPath findShortestPath(List<Section> sections, long departureId, long arrivalId) {
        WeightedMultigraph<Long, LineEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addStationVertex(sections, graph);
        addSectionEdge(sections, graph);
        GraphPath path = getGraphPath(departureId, arrivalId, graph);
        validateConnection(path);
        return path;
    }

    private void validateConnection(GraphPath path) {
        if (path == null) {
            throw new IllegalArgumentException("연결되지 않은 구간입니다.");
        }
    }

    private GraphPath getGraphPath(long departureId, long arrivalId,
                                   WeightedMultigraph<Long, LineEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(departureId, arrivalId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 등록 되지 않은 역입니다.");
        }
    }

    private void addStationVertex(List<Section> sections, WeightedMultigraph<Long, LineEdge> graph) {
        for (long stationId : getAllStationIds(sections)) {
            graph.addVertex(stationId);
        }
    }

    private List<Long> getAllStationIds(List<Section> sections) {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return new ArrayList<>(stationIds);
    }

    private void addSectionEdge(List<Section> sections, WeightedMultigraph<Long, LineEdge> graph) {
        for (Section section : sections) {
            int distance = section.getDistance();
            long lineId = section.getLineId();
            graph.addEdge(section.getUpStationId(), section.getDownStationId(), new LineEdge(lineId, distance));
        }
    }

    private List<Long> getShortestPathLineIds(GraphPath graphPath) {
        List<LineEdge> edges = graphPath.getEdgeList();
        return edges.stream()
                .map(LineEdge::getLindId)
                .distinct()
                .collect(Collectors.toList());
    }
}
