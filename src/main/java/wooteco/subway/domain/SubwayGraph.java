package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NoSuchPathException;

import java.util.List;
import java.util.Objects;

public class SubwayGraph {

    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int LEVEL_ONE_ADDITIONAL_DISTANCE = 50;
    private static final int ADDITIONAL_BASIC_FARE = 800;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> graph;

    public SubwayGraph(final List<Section> sections) {
        graph = initGraph(sections);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
        return new DijkstraShortestPath<>(graph);
    }

    public Path getPath(final Station source, final Station target) {
        validateExistsPath(source, target);
        List<Station> stations = graph.getPath(source, target).getVertexList();
        int distance = (int) graph.getPathWeight(source, target);
        int fare = BASIC_FARE + calculateFare(distance);
        return new Path(stations, distance, fare);
    }

    private void validateExistsPath(final Station source, final Station target) {
        if (Objects.isNull(this.graph.getPath(source, target))) {
            throw new NoSuchPathException(source.getId(), target.getId());
        }
    }

    private int calculateFare(int distance) {
        if (distance < BASIC_DISTANCE) {
            return 0;
        }

        if (distance < LEVEL_ONE_ADDITIONAL_DISTANCE) {
            return (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100);
        }

        return ADDITIONAL_BASIC_FARE + (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100);
    }
}
