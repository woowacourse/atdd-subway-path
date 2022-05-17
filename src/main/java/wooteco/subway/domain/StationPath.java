package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.utils.exception.StationNotFoundException;

public class StationPath {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_FARE_DISTANCE = 50;
    private static final int STANDARD_UNIT = 5;
    private static final int MAX_UNIT = 8;

    private final Sections sections;

    public StationPath(final Sections sections) {
        this.sections = sections;
    }

    public int calculateMinDistance(final Station startStation, final Station endStation) {
        validateExistStation(startStation, endStation);
        return (int) createSectionDijkstraShortestPath().getPathWeight(startStation, endStation);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createSectionDijkstraShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sections.sortSections()) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            assignWeight(graph, section);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void assignWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    private void validateExistStation(Station startStation, Station endStation) {
        if (!sections.isExistStation(startStation) || !sections.isExistStation(endStation)) {
            throw new StationNotFoundException();
        }
    }

    public List<Station> findShortestStations(final Station startStation, final Station endStation) {
        return createSectionDijkstraShortestPath().getPath(startStation, endStation).getVertexList();
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
