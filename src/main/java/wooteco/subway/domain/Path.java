package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath dijkstraShortestPath;

    public Path(Sections sections) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        addStationVertex(graph, sections);
        addSectionEdge(graph, sections);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void addStationVertex(WeightedMultigraph<String, DefaultWeightedEdge> graph, Sections sections) {
        for (Long stationId : sections.getAllStationIds()) {
            graph.addVertex(String.valueOf(stationId));
        }
    }

    private void addSectionEdge(WeightedMultigraph<String, DefaultWeightedEdge> graph, Sections sections) {
        for (List<Number> sectionInfo : sections.getSectionInfos()) {
            String upStationId = String.valueOf(sectionInfo.get(0));
            String downStationId = String.valueOf(sectionInfo.get(1));
            int distance = (int) sectionInfo.get(2);
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), distance);
        }
    }

    public List<Long> getShortestPathStationIds(Long departureId, Long arrivalId) {
        GraphPath graphPath = getGraphPath(departureId, arrivalId);
        validateConnection(graphPath);
        List<String> stationIds = graphPath.getVertexList();
        return stationIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public int getShortestPathDistance(Long departureId, Long arrivalId) {
        GraphPath graphPath = getGraphPath(departureId, arrivalId);
        validateConnection(graphPath);
        return (int) graphPath.getWeight();
    }

    private GraphPath getGraphPath(Long departureId, Long arrivalId) {
        try {
            return dijkstraShortestPath.getPath(String.valueOf(departureId), String.valueOf(arrivalId));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("구간에 등록 되지 않은 역입니다.");
        }
    }

    private void validateConnection(GraphPath graphPath) {
        if (graphPath == null) {
            throw new IllegalArgumentException("연결되지 않은 구간입니다.");
        }
    }
}
