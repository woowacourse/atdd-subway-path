package wooteco.subway.infra;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public abstract class ShortestPathStrategy {

    private final Multigraph<Station, ShortestPathEdge> graph;

    public ShortestPathStrategy(List<Section> sections) {
        this.graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertex(sections, graph);
        addEdge(sections, graph);
    }

    public abstract ShortestPathAlgorithm createShortestPath();

    public Multigraph<Station, ShortestPathEdge> getGraph() {
        return graph;
    }

    private void addVertex(List<Section> sections, Multigraph<Station, ShortestPathEdge> graph) {
        Set<Station> stations = getStations(sections);
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdge(List<Section> sections, Multigraph<Station, ShortestPathEdge> graph) {
        for (Section section : sections) {
            Line line = section.getLine();
            int distance = section.getDistance();
            graph.addEdge(section.getUpStation(), section.getDownStation(), new ShortestPathEdge(line.getExtraFare(), distance));
        }
    }

    private static Set<Station> getStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .collect(Collectors.toUnmodifiableSet());
    }
}
