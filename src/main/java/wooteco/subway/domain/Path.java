package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private List<Long> shortestPath;
    private int totalDistance;

    public Path(Long source, Long target, List<Long> stationIds, Sections sections) {
        validateExistStationId(source, target, stationIds);
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph(stationIds, sections);
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validateExistPath(path);
        this.shortestPath = path.getVertexList();
        this.totalDistance = (int) path.getWeight();
    }

    private void validateExistPath(GraphPath<Long, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 수 없습니다");
        }
    }

    private void validateExistStationId(Long source, Long target, List<Long> stationIds) {
        if (isNotContains(source, target, stationIds)){
            throw new IllegalArgumentException("[ERROR] 역을 찾을 수 없습니다");
        }
    }

    private boolean isNotContains(Long source, Long target, List<Long> stationIds) {
        return !(stationIds.contains(source) && stationIds.contains(target));
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Long> stationId, Sections sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(stationId, graph);
        addEdges(sections, graph);
        return graph;
    }

    private void addVertexes(List<Long> stationId, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Long id : stationId) {
            graph.addVertex(id);
        }
    }

    private void addEdges(Sections sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistance());
        }
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
