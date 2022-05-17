package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import java.util.List;

public class Path {

    private static final int MINIMUM_FARE = 1250;
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
        return calculateFare(getDistance(source, target));
    }

    private int calculateFare(final double distance) {
        return MINIMUM_FARE;
    }
}
