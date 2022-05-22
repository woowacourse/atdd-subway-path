package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathCalculator {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathCalculator(final List<Line> lines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        for (Line line : lines) {
            final List<Section> sections = line.getSections();
            addSectionInGraph(graph, sections);
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addSectionInGraph(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
                                   final List<Section> sections) {
        for (Section section : sections) {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
            graph.setEdgeWeight(graph.addEdge(downStation, upStation), section.getDistance());
        }
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(final Station source, final Station target) {
        return dijkstraShortestPath.getPath(source, target);
    }
}
