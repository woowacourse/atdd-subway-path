package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.fare.AgeFarePolicy;
import wooteco.subway.domain.fare.DistanceFarePolicy;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.distance.Kilometer;

public class Path {

    private final List<Station> stations;
    private final Kilometer distance;
    private final Fare extraFare;

    public Path(List<Station> stations, Kilometer distance, Fare extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public static Path of(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, JgraphtPath> graph = new WeightedMultigraph<>(JgraphtPath.class);
        addVertex(graph, lines);
        addEdge(graph, lines);
        DijkstraShortestPath<Station, JgraphtPath> algorithms = new DijkstraShortestPath<>(graph);
        GraphPath<Station, JgraphtPath> graphPath = algorithms.getPath(source, target);
        return new Path(graphPath.getVertexList(), Kilometer.from((int)graphPath.getWeight()),
                new Fare(getExtraFare(graphPath)));
    }

    private static int getExtraFare(GraphPath<Station, JgraphtPath> graphPath) {
        return graphPath.getEdgeList().stream()
                .mapToInt(JgraphtPath::getExtraFare)
                .max()
                .orElse(0);
    }

    private static void addVertex(WeightedMultigraph<Station, JgraphtPath> graph, List<Line> lines) {
        for (Station station : getStations(lines)) {
            graph.addVertex(station);
        }
    }

    private static Set<Station> getStations(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet());
    }

    private static void addEdge(WeightedMultigraph<Station, JgraphtPath> graph, List<Line> lines) {
        for (Line line : lines) {
            addEdge(graph, line.getSections(), line.getExtraFare());
        }
    }

    private static void addEdge(WeightedMultigraph<Station, JgraphtPath> graph, Sections sections, int extraFare) {
        for (Section section : sections.getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new JgraphtPath(section.getDistance(), extraFare));
        }
    }

    public Kilometer getDistance() {
        return distance;
    }

    public Fare getFare(Age age) {
        Fare fareOfPath = DistanceFarePolicy.getFare(distance)
                .add(extraFare);
        return AgeFarePolicy.getFare(fareOfPath, age);
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }
}
