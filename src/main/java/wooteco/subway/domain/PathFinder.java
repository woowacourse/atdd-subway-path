package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.PathNotFoundException;

public class PathFinder {

    private final DijkstraShortestPath<Station, ExtraFareSubwayEdge> dijkstraShortestPath;

    private PathFinder(DijkstraShortestPath<Station, ExtraFareSubwayEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder from(List<Line> lines) {
        return new PathFinder(new DijkstraShortestPath<>(initGraph(lines)));
    }

    private static WeightedMultigraph<Station, ExtraFareSubwayEdge> initGraph(List<Line> lines) {
        WeightedMultigraph<Station, ExtraFareSubwayEdge> graph = new WeightedMultigraph<>(
            ExtraFareSubwayEdge.class);
        for (Line line : lines) {
            initVertex(graph, line);
            initEdge(graph, line);
        }
        return graph;
    }

    private static void initVertex(WeightedMultigraph<Station, ExtraFareSubwayEdge> graph,
        Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private static void initEdge(WeightedMultigraph<Station, ExtraFareSubwayEdge> graph,
        Line line) {
        for (Section section : line.getSections().getSections()) {
            ExtraFareSubwayEdge edge = new ExtraFareSubwayEdge(line.getExtraFare());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    public Path findShortestPath(Station source, Station target) {
        validateSection(source, target);
        try {
            return findPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathNotFoundException(
                String.format("%s부터 %s까지의 경로가 존재하지 않습니다.", source.getName(), target.getName()));
        }
    }

    private void validateSection(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathNotFoundException(
                String.format("출발역 %s와 도착역 %s가 동일할 수 없습니다.", source.getName(), target.getName()));
        }
    }

    private Path findPath(Station source, Station target) {
        GraphPath<Station, ExtraFareSubwayEdge> path = dijkstraShortestPath.getPath(source, target);
        validateEmptyPath(source, target, path);
        List<Integer> extraFares = toExtraFares(path);
        return new Path(path.getVertexList(), extraFares, (int) path.getWeight());
    }

    private List<Integer> toExtraFares(GraphPath<Station, ExtraFareSubwayEdge> path) {
        return path.getEdgeList().stream()
            .map(ExtraFareSubwayEdge::getExtraFare)
            .collect(Collectors.toList());
    }

    private void validateEmptyPath(Station source, Station target,
        GraphPath<Station, ExtraFareSubwayEdge> path) {
        if (path == null) {
            throw new PathNotFoundException(
                String.format("%s부터 %s까지의 경로가 존재하지 않습니다.", source.getName(), target.getName()));
        }
    }
}
