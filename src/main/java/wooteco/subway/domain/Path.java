package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;
    private static final int LEVEL_ONE_ADDITIONAL_DISTANCE = 50;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public Path(final List<Section> sections) {
        path = initPath(sections);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initPath(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initGraph(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> findRoute(final Station source, final Station target) {
        return path.getPath(source, target).getVertexList();
    }

    public int calculateDistance(final Station source, final Station target) {
        return (int) path.getPath(source, target).getWeight();
    }

    public int calculateFare(final Station source, final Station target) {
        int distance = (int) path.getPathWeight(source, target);
        return BASIC_FARE + calculateFare(distance);
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
        if (distance < BASIC_DISTANCE) {
            return 0;
        }

        if (distance < LEVEL_ONE_ADDITIONAL_DISTANCE) {
            return (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100);
        }

        return 800 + (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100);
    }
}
