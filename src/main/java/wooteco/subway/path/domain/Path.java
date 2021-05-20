package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

    public Path(final List<Line> lines) {
        for (final Line line : lines) {
            addSections(line.getSectionsAsList());
        }
    }

    private void addSections(final List<Section> sections) {
        for (final Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    public List<Station> route(final Station source, final Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public int distance(final Station source, final Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }
}
