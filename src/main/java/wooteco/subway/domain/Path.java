package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final List<Long> shortestPath;
    private final int totalDistance;
    private final Sections sections;

    private Path(List<Long> shortestPath, int totalDistance, Sections sections) {
        this.shortestPath = shortestPath;
        this.totalDistance = totalDistance;
        this.sections = sections;
    }

    public static Path of(Long source, Long target, List<Long> stationIds, Sections sections) {
        validateExistStationId(source, target, stationIds);
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph(stationIds, sections);
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validateExistPath(path);
        return new Path(path.getVertexList(), (int) path.getWeight(), sections);
    }

    private static void validateExistPath(GraphPath<Long, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 수 없습니다");
        }
    }

    private static void validateExistStationId(Long source, Long target, List<Long> stationIds) {
        if (isNotContains(source, target, stationIds)) {
            throw new IllegalArgumentException("[ERROR] 역을 찾을 수 없습니다");
        }
    }

    private static boolean isNotContains(Long source, Long target, List<Long> stationIds) {
        return !(stationIds.contains(source) && stationIds.contains(target));
    }

    private static WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Long> stationId, Sections sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(stationId, graph);
        addEdges(sections, graph);
        return graph;
    }

    private static void addVertexes(List<Long> stationId, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Long id : stationId) {
            graph.addVertex(id);
        }
    }

    private static void addEdges(Sections sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<Section> getShortestPathSections() {
        return sections.getSectionsFromShortestPath(shortestPath);
    }
}
