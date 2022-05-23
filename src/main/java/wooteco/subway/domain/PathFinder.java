package wooteco.subway.domain;

import java.util.List;
import java.util.NoSuchElementException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

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
        final int extraFare = getMaxExtraFare(graphPath);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), extraFare);
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

    private int getMaxExtraFare(final GraphPath<Station, SubwayWeightEdge> graphPath) {
        return graphPath.getEdgeList()
                .stream()
                .mapToInt(SubwayWeightEdge::getExtraFare)
                .max()
                .orElseThrow(() -> new NoSuchElementException("가장 높은 금액의 추가 요금을 찾는 도중 오류가 발생했습니다."));
    }
}
