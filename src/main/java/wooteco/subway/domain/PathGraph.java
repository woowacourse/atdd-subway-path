package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.PathNotFoundException;

public class PathGraph {

    private final WeightedMultigraph<Station, ExtraFareSubwayEdge> graph;

    public PathGraph(List<Line> lines) {
        graph = new WeightedMultigraph<>(ExtraFareSubwayEdge.class);
        initGraph(lines);
    }

    private void initGraph(List<Line> lines) {
        for (Line line : lines) {
            initVertex(line);
            initEdge(line);
        }
    }

    private void initVertex(Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void initEdge(Line line) {
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
            throw throwNotFoundPath(source, target);
        }
    }

    private void validateSection(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathNotFoundException(
                String.format("출발역 %s와 도착역 %s가 동일할 수 없습니다.", source.getName(), target.getName()));
        }
    }

    private Path findPath(Station source, Station target) {
        DijkstraShortestPath<Station, ExtraFareSubwayEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(graph);
        GraphPath<Station, ExtraFareSubwayEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw throwNotFoundPath(source, target);
        }
        List<Integer> extraFares = path.getEdgeList().stream()
            .map(ExtraFareSubwayEdge::getExtraFare)
            .collect(Collectors.toList());
        return new Path(path.getVertexList(), extraFares, (int) path.getWeight());
    }

    private PathNotFoundException throwNotFoundPath(Station source, Station target) {
        throw new PathNotFoundException(
            String.format("%s부터 %s까지의 경로가 존재하지 않습니다.", source.getName(), target.getName()));
    }
}
