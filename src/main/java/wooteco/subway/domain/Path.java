package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private static final int BASIC_FARE = 1250;
    private static final int SHORT_DISTANCE_UPPER_BOUND = 10;
    private static final int MIDDLE_DISTANCE_UPPER_BOUND = 50;
    private static final int ADDITIONAL_FARE = 100;
    public static final int MIDDLE_DISTANCE_FARE_RATIO = 5;
    public static final int LONG_DISTANCE_FARE_RATIO = 8;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public Path(final List<Section> sections) {
        path = initPath(sections);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initPath(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> findRoute(final Station source, final Station target) {
        checkRouteExist(source, target);
        return path.getPath(source, target).getVertexList();
    }

    public int calculateDistance(final Station source, final Station target) {
        checkRouteExist(source, target);
        return (int) path.getPath(source, target).getWeight();
    }

    public int calculateFare(final Station source, final Station target) {
        checkRouteExist(source, target);
        int distance = (int) path.getPathWeight(source, target);
        return BASIC_FARE + calculateFare(distance);
    }

    private void checkRouteExist(final Station source, final Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = this.path.getPath(source, target);
        if (path == null) {
            throw new IllegalArgumentException("이동 가능한 경로가 존재하지 않습니다");
        }
    }

    private void initGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                           final List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private int calculateFare(int distance) {
        if (distance < SHORT_DISTANCE_UPPER_BOUND) {
            return 0;
        }

        if (distance <= MIDDLE_DISTANCE_UPPER_BOUND) {
            int distanceForPayingAdditionalAmount = distance - SHORT_DISTANCE_UPPER_BOUND;
            return middleDistanceUnit(distanceForPayingAdditionalAmount) * ADDITIONAL_FARE;
        }

        int distanceForPayingAdditionalAmount = distance - MIDDLE_DISTANCE_UPPER_BOUND;
        return fullMiddleDistanceAdditionalFare() + longDistanceUnit(distanceForPayingAdditionalAmount);
    }

    private int middleDistanceUnit(final int distanceForPayingAdditionalAmount) {
        return (int) Math.ceil((distanceForPayingAdditionalAmount - 1) / MIDDLE_DISTANCE_FARE_RATIO) + 1;
    }

    private int longDistanceUnit(final int distanceForPayingAdditionalAmount) {
        return (int) (Math.ceil((distanceForPayingAdditionalAmount - 1) / LONG_DISTANCE_FARE_RATIO) + 1)
                * ADDITIONAL_FARE;
    }

    private int fullMiddleDistanceAdditionalFare() {
        return (MIDDLE_DISTANCE_UPPER_BOUND - SHORT_DISTANCE_UPPER_BOUND) / MIDDLE_DISTANCE_FARE_RATIO
                * ADDITIONAL_FARE;
    }
}
