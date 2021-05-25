package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final List<Station> stations;
    private final PathStrategy pathStrategy;

    public Path(List<Station> stations, List<Line> lines, PathStrategy pathStrategy) {
        this.stations = new ArrayList<>(stations);
        this.pathStrategy = pathStrategy;
        initializeGraph(lines);
    }

    private void initializeGraph(List<Line> lines) {
        lines.forEach(
                line -> line.getEachSections()
                        .forEach(this::addSectionsToGraph)
        );
    }

    private void addSectionsToGraph(Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public GraphPath<Station, DefaultWeightedEdge> calculateShortestPath(Station source, Station target) {
        return this.pathStrategy.calculateShortestPath(graph, stations, source, target);
    }
}
