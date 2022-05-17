package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import java.util.List;

public class Path {

    private static final int MINIMUM_FARE_FEE = 1250;
    private static final int MINIMUM_DISTANCE_BOUNDARY = 10;
    private static final int OVER_FARE_FEE = 100;
    private static final int BELOW_FIFTY_KM_POLICY = 5;
    private static final int ABOVE_FIFTY_KM_POLICY = 8;
    private static final int MAXIMUM_DISTANCE_BOUNDARY = 50;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static Path from(final Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.extractStations()) {
            graph.addVertex(station);
        }

        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return new Path(graph);
    }

    public List<Station> getVertexList(final Station source, final Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public DijkstraShortestPath<Station, DefaultWeightedEdge> getDijkstraShortestPath() {
        return dijkstraShortestPath;
    }

    public double getDistance(final Station source, final Station target) {
        return dijkstraShortestPath.getPath(source, target).getWeight();
    }

    public int getFare(final Station source, final Station target) {
        final double distance = getDistance(source, target);

        if (distance <= MINIMUM_DISTANCE_BOUNDARY) {
            return MINIMUM_FARE_FEE;
        }

        return MINIMUM_FARE_FEE + calculateFareByDistance(distance);
    }

    private int calculateFareByDistance(final double distance) {
        if (distance <= MAXIMUM_DISTANCE_BOUNDARY) {
            return calculateFare(distance  - MINIMUM_DISTANCE_BOUNDARY, BELOW_FIFTY_KM_POLICY);
        }

        return 800 + calculateFare(distance - MAXIMUM_DISTANCE_BOUNDARY, ABOVE_FIFTY_KM_POLICY);
    }

    private int calculateFare(final double overFaredDistance, final int policy) {
        return (int) ((Math.ceil((overFaredDistance - 1) / policy)) * OVER_FARE_FEE);
    }
}
