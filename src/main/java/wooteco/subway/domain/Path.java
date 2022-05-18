package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.exception.SectionNotFoundException;
import wooteco.subway.utils.DijkstraGraphGenerator;

public class Path {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_FARE_DISTANCE = 50;
    private static final int STANDARD_UNIT = 5;
    private static final int MAX_UNIT = 8;

    private final Station startStation;
    private final Station endStation;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;

    public Path(Station startStation, Station endStation, DijkstraShortestPath<Station, DefaultWeightedEdge> graph) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.graph = graph;
    }

    public static Path of(final Station startStation, final Station endStation, final Sections sections) {
        return new Path(startStation, endStation, DijkstraGraphGenerator.createSectionDijkstraShortestPath(sections));
    }

    public int calculateMinDistance() {
        try {
            return (int) graph.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public List<Station> findShortestStations() {
        try {
            return graph.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public int calculateFare(final int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= OVER_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateOverFare(distance - DEFAULT_DISTANCE, STANDARD_UNIT);
        }
        return DEFAULT_FARE
                + calculateOverFare(OVER_FARE_DISTANCE - DEFAULT_DISTANCE, STANDARD_UNIT)
                + calculateOverFare(distance - OVER_FARE_DISTANCE, MAX_UNIT);
    }

    private int calculateOverFare(final int distance, final int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }
}
