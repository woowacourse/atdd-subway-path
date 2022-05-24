package wooteco.subway.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Fare.Fare;
import wooteco.subway.domain.distance.Kilometer;
import wooteco.subway.domain.path.JgraphtPath;
import wooteco.subway.domain.path.ShortestPath;

public class PathCalculator {

    public static ShortestPath getShortestPath(List<Line> lines, Station source, Station target) {
        WeightedMultigraph<Station, JgraphtPath> graph = new WeightedMultigraph<>(JgraphtPath.class);
        addVertex(graph, lines);
        addEdge(graph, lines);
        DijkstraShortestPath<Station, JgraphtPath> algorithms = new DijkstraShortestPath<>(graph);
        return getShortestPath(algorithms.getPath(source, target));
    }

    private static void addVertex(WeightedMultigraph<Station, JgraphtPath> graph, List<Line> lines) {
        Set<Station> stations = lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet());
        for (Station station : stations) {
            graph.addVertex(station);
        }
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

    private static ShortestPath getShortestPath(GraphPath<Station, JgraphtPath> graphPath) {
        int distance = (int) graphPath.getWeight();
        int extraFare = graphPath.getEdgeList().stream()
                .mapToInt(JgraphtPath::getExtraFare)
                .max()
                .orElse(0);
        List<Station> stations1 = graphPath.getVertexList();
        return new ShortestPath(stations1, Kilometer.from(distance), new Fare(extraFare));
    }
}
