package wooteco.subway.infrastructure;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathCalculator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class PathCalculatorDijkstra implements PathCalculator {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathCalculatorDijkstra(final List<Line> lines) {
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

    @Override
    public Path findShortestPath(final Station source, final Station target) {
        return new SubwayPath(dijkstraShortestPath.getPath(source, target));
    }
}
