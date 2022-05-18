package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {

    private static final int BASE_FEE = 1250;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public SubwayGraph(List<Section> sections) {
        this.path = createPath(new ArrayList<>(sections));
    }

    public List<Station> getShortestRoute(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> result = path.getPath(source, target);
        validateRoute(result);
        return result.getVertexList();
    }

    public int getShortestDistance(Station source, Station target) {
        return (int) path.getPath(source, target).getWeight();
    }

    public int getFare(Station source, Station target) {
        return calculateOverFare(getShortestDistance(source, target));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createPath(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void validateRoute(GraphPath<Station, DefaultWeightedEdge> result) {
        if (result == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

    private int calculateOverFare(int distance) {
        if (distance <= 10) {
            return BASE_FEE;
        }
        if (distance <= 50) {
            return (int) ((Math.ceil((distance - 10 - 1) / 5) + 1) * 100) + BASE_FEE;
        }
        return (int) ((Math.ceil((distance - 50 - 1) / 8) + 1) * 100) + 2050;
    }
}
