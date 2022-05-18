package wooteco.subway.domain;

import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private static final String NOT_EXIST_STATION = "출발지, 도착지 모두 존재해야 됩니다.";
    private static final String NO_REACHABLE = "출발지에서 도착지로 갈 수 없습니다.";

    private final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<Long, DefaultWeightedEdge>(
            DefaultWeightedEdge.class);

    public Path(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStationId());
            graph.addVertex(section.getDownStationId());
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }

    public List<Long> calculateShortestPath(Long source, Long target) {
        Optional<GraphPath> path = makeGraphPath(source, target);

        return path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE)).getVertexList();
    }

    public int calculateShortestDistance(Long source, Long target) {
        Optional<GraphPath> path = makeGraphPath(source, target);

        return (int) path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE)).getWeight();
    }

    private Optional<GraphPath> makeGraphPath(Long source, Long target) {
        Optional<GraphPath> path;

        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            path = Optional.ofNullable(
                    dijkstraShortestPath.getPath(source, target));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(NOT_EXIST_STATION);
        }

        return path;
    }
}
