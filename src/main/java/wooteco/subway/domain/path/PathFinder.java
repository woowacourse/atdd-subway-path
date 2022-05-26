package wooteco.subway.domain.path;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class PathFinder {

    final private DijkstraShortestPath<Station, SubwayWeightEdge> dijkstraShortestPath;

    public PathFinder(final List<Line> lines) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(createGraph(lines));
    }

    private WeightedMultigraph<Station, SubwayWeightEdge> createGraph(final List<Line> lines) {
        final WeightedMultigraph<Station, SubwayWeightEdge> graph = new WeightedMultigraph<>(SubwayWeightEdge.class);
        for (final Line line : lines) {
            addVertex(line, graph);
            addEdge(line, graph);
        }
        return graph;
    }

    private void addVertex(final Line line, final WeightedMultigraph<Station, SubwayWeightEdge> graph) {
        for (final Station station : line.getSortedStations()) {
            graph.addVertex(station);
        }
    }

    private void addEdge(final Line line, final WeightedMultigraph<Station, SubwayWeightEdge> graph) {
        for (final Section section : line.getSections().getValues()) {
            final SubwayWeightEdge weight = new SubwayWeightEdge(line, section.getDistance());
            graph.addEdge(section.getUpStation(), section.getDownStation(), weight);
            graph.addEdge(section.getDownStation(), section.getUpStation(), weight);
        }
    }

    public Path find(final Station source, final Station target) {
        validateSameStation(source, target);
        final GraphPath<Station, SubwayWeightEdge> graphPath = getShortestPath(source, target);
        return new Path(graphPath.getVertexList(), graphPath.getEdgeList(), (int) graphPath.getWeight());
    }

    private void validateSameStation(final Station source, final Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
    }

    private GraphPath<Station, SubwayWeightEdge> getShortestPath(final Station source, final Station target) {
        final GraphPath<Station, SubwayWeightEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        validateGraphPath(graphPath);
        return graphPath;
    }

    private void validateGraphPath(GraphPath<Station, SubwayWeightEdge> graphPath) {
        if (null == graphPath) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }
    }
}
