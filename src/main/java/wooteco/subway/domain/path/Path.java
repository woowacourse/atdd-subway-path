package wooteco.subway.domain.path;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class Path {

    private static final int BASE_FARE = 1250;
    private static final int FIRST_DEFAULT_FARE = 800;
    private static final int FIRST_SURCHARGE_DISTANCE = 10;
    private static final int SECOND_SURCHARGE_DISTANCE = 50;
    private static final int FIRST_SURCHARGE_DIVIDE_DISTANCE = 5;
    private static final int SECOND_SURCHARGE_DIVIDE_DISTANCE = 8;

    private final List<Station> stations;
    private final int distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        initializeGraph(lines, graph);
        GraphPath<Station, PathEdge> path = new DijkstraShortestPath(graph).getPath(source, target);
        validatePath(path);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private static void initializeGraph(List<Line> lines, WeightedMultigraph<Station, PathEdge> graph) {
        for (Line line : lines) {
            initVertex(graph, line);
            initEdge(graph, line);
        }
    }

    private static void initEdge(WeightedMultigraph<Station, PathEdge> graph, Line line) {
        for (Section section : line.getSections().getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    PathEdge.from(section));
        }
    }

    private static void initVertex(WeightedMultigraph<Station, PathEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private static void validatePath(GraphPath<Station, PathEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

    public int calculateFare() {
        if (distance <= FIRST_SURCHARGE_DISTANCE) {
            return BASE_FARE;
        }
        if (distance <= SECOND_SURCHARGE_DISTANCE) {
            return BASE_FARE + calculateOverFare(distance - FIRST_SURCHARGE_DISTANCE,
                    FIRST_SURCHARGE_DIVIDE_DISTANCE);
        }
        return BASE_FARE + FIRST_DEFAULT_FARE + calculateOverFare(
                distance - SECOND_SURCHARGE_DISTANCE,
                SECOND_SURCHARGE_DIVIDE_DISTANCE);
    }

    private int calculateOverFare(int distance, int divideDistance) {
        return (int) ((Math.ceil((distance - 1) / divideDistance) + 1) * 100);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
