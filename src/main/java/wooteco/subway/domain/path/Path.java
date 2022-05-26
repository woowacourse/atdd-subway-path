package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.distance.Kilometer;
import wooteco.subway.domain.fare.Fare;

public class Path {

    private final List<Station> stations;
    private final Kilometer distance;
    private final Fare extraFare;

    public Path(List<Station> stations, Kilometer distance, Fare extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public static Path of(Map<Section, Fare> sectionWithExtraFare, Station source, Station target) {
        WeightedMultigraph<Station, JgraphtPath> graph = new WeightedMultigraph<>(JgraphtPath.class);
        addVertex(sectionWithExtraFare, graph);
        addEdge(sectionWithExtraFare, graph);
        DijkstraShortestPath<Station, JgraphtPath> algorithms = new DijkstraShortestPath<>(graph);
        GraphPath<Station, JgraphtPath> graphPath = algorithms.getPath(source, target);
        return new Path(graphPath.getVertexList(), Kilometer.from((int) graphPath.getWeight()),
                new Fare(getExtraFare(graphPath)));
    }

    private static void addVertex(Map<Section, Fare> sectionWithExtraFare,
                                  WeightedMultigraph<Station, JgraphtPath> graph) {
        Set<Station> stations = sectionWithExtraFare.keySet()
                .stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(Collectors.toSet());
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(Map<Section, Fare> sectionWithExtraFare,
                                WeightedMultigraph<Station, JgraphtPath> graph) {
        sectionWithExtraFare.forEach(((section, fare) ->
                graph.addEdge(section.getUpStation(), section.getDownStation(),
                        new JgraphtPath(section.getDistance(), fare.value()))));
    }

    private static int getExtraFare(GraphPath<Station, JgraphtPath> graphPath) {
        return graphPath.getEdgeList().stream()
                .mapToInt(JgraphtPath::getExtraFare)
                .max()
                .orElse(0);
    }

    public Kilometer getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }

    public Fare getExtraFare() {
        return extraFare;
    }
}
