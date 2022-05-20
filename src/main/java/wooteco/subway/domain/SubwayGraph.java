package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {

    private static final double FIVE_KM = 5.0;
    private static final double EIGHT_KM = 8.0;
    private static final int OVER_FEE = 100;
    private static final int FEE_BASE = 1250;
    private static final int FEE_50KM = 2050;
    private static final int BASE_FEE_DISTANCE = 10;
    private static final int OVER_FEE_DISTANCE = 50;

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

    public int calculateFare(Station source, Station target) {
        return calculateOverFare(getShortestDistance(source, target));
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createPath(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void validateRoute(GraphPath<Station, DefaultWeightedEdge> route) {
        if (route == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASE_FEE_DISTANCE) {
            return FEE_BASE;
        }
        if (distance <= OVER_FEE_DISTANCE) {
            return (int) ((Math.ceil((distance - BASE_FEE_DISTANCE) / FIVE_KM)) * OVER_FEE)
                    + FEE_BASE;
        }
        return (int) ((Math.ceil((distance - OVER_FEE_DISTANCE) / EIGHT_KM)) * OVER_FEE) + FEE_50KM;
    }
}
