package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final GraphPath<Long, DefaultWeightedEdge> path;

    private Path(GraphPath<Long, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public static Path of(Sections sections, Long sourceId, Long targetId) {
        final List<Long> stationIds = sections.getStationIds();
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(graph, stationIds);
        addEdges(graph, sections.getSections());
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return new Path(dijkstraShortestPath.getPath(sourceId, targetId));
    }

    private static void addVertexes(WeightedMultigraph<Long, DefaultWeightedEdge> graph, List<Long> stationIds) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private static void addEdges(WeightedMultigraph<Long, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }

    public List<Long> getShortestPath() {
        return path.getVertexList();
    }

    public int getShortestPathWeight() {
        return (int) path.getWeight();
    }
}
