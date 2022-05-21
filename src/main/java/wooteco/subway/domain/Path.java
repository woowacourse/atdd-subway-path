package wooteco.subway.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final List<Line> lines;
    private final GraphPath<Station, DefaultWeightedEdge> path;

    public Path(List<Line> lines, Station source, Station target) {
        this.lines = List.copyOf(lines);
        this.path = new DijkstraShortestPath<>(initGraph(getSections())).getPath(source, target);
    }

    public List<Station> getStations() {
        return path.getVertexList();
    }

    private List<Section> getSections() {
        return lines.stream()
            .map(Line::getSections)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private Graph<Station, DefaultWeightedEdge> initGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            addSection(graph, section);
        }
        return graph;
    }

    private void addSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(
            graph.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistance()
        );
    }

    public int getDistance() {
        return (int)path.getWeight();
    }

    public int getOverFare() {
        return 0;
    }
}
