package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private static final int BASIC_FARE = 1250;
    private static final int  BASIC_DISTANCE= 10;
    private static final int LEVEL_ONE_ADDITIONAL_DISTANCE = 50;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public PathFinder(final List<Line> lines) {
        path = initPath(lines);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> initPath(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(graph, lines);
        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> findPath(final Station source, final Station target) {
        return path.getPath(source, target).getVertexList();
    }

    public int getMinDistance(final Station source, final Station target) {
        return (int) path.getPath(source, target).getWeight();
    }

    private void initGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                           final List<Line> lines) {
        for (Line line : lines) {
            addVertexAndEdge(graph, line);
        }
    }

    private void addVertexAndEdge(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Line line) {
        List<Section> sections = line.getSections();
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    public int calculateFare(final Station source, final Station target) {
        int distance = (int) path.getPathWeight(source, target);
        return  BASIC_FARE + calculateFare(distance);
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
