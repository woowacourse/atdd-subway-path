package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class NewPathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public NewPathFinder(final List<Line> lines) {
        for (final Line line : lines) {
            addSections(line.getSectionsAsList());
        }
    }

    private void addSections(final List<Section> sections) {
        for (final Section section : sections) {
            //  If this graph already contains such vertex, 'addVertex' leaves this graph unchanged.
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    public NewPathFinder update(final List<Line> lines){
        return new NewPathFinder(lines);
    }

    public GraphPath shortest(final Station source, final Station target) {
        return new DijkstraShortestPath(graph).getPath(source, target);
    }
}
