package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.PathNotFoundException;

public class PathGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathGraph(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
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
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
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
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        if (path == null) {
            throw throwNotFoundPath(source, target);
        }
        return new Path(path.getVertexList(), (int) path.getWeight());
    }

    private PathNotFoundException throwNotFoundPath(Station source, Station target) {
        throw new PathNotFoundException(
            String.format("%s부터 %s까지의 경로가 존재하지 않습니다.", source.getName(), target.getName()));
    }
}
