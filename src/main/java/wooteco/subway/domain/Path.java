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

public class Path {

    private final GraphPath graphPath;

    private Path(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public static Path of(List<Section> sections, Long departureId, Long arrivalId) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addStationVertex(graph, sections);
        addSectionEdge(graph, sections);
        GraphPath graphPath = findShortestPath(departureId, arrivalId, graph);
        return new Path(graphPath);
    }

    private static void addStationVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph,
                                         List<Section> sections) {
        for (Long stationId : getAllStationIds(sections)) {
            graph.addVertex(String.valueOf(stationId));
        }
    }

    private static List<Long> getAllStationIds(List<Section> sections) {
        Set<Long> stationIds = new HashSet<>();

        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }

        return new ArrayList<>(stationIds);
    }

    private static void addSectionEdge(WeightedMultigraph<String, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            String upStationId = String.valueOf(section.getUpStationId());
            String downStationId = String.valueOf(section.getDownStationId());
            int distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), distance);
        }
    }

    private static GraphPath findShortestPath(Long departureId, Long arrivalId,
                                              WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        GraphPath path = getGraphPath(departureId, arrivalId, graph);
        validateConnection(path);
        return path;
    }

    private static GraphPath getGraphPath(Long departureId, Long arrivalId,
                                          WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(String.valueOf(departureId), String.valueOf(arrivalId));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 등록 되지 않은 역입니다.");
        }
    }

    private static void validateConnection(GraphPath path) {
        if (path == null) {
            throw new IllegalArgumentException("연결되지 않은 구간입니다.");
        }
    }

    public List<Long> getShortestPathStationIds() {
        List<String> stationIds = graphPath.getVertexList();
        return stationIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public int getShortestPathDistance() {
        return (int) graphPath.getWeight();
    }
}
