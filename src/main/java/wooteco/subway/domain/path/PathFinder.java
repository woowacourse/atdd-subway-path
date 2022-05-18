package wooteco.subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

import java.util.List;

public class PathFinder {
    private final GraphPath<Station, DefaultWeightedEdge> path;

    private PathFinder(GraphPath<Station, DefaultWeightedEdge> path) {
        this.path = path;
    }

    public static PathFinder of(List<Section> sections, Station source, Station target) {
        validateDistinctStation(source, target);
        GraphPath<Station, DefaultWeightedEdge> path = generatePath(sections, source, target);
        validatePath(path);

        return new PathFinder(path);
    }

    public List<Station> findShortestPath() {
        return path.getVertexList();
    }

    public int findDistance() {
        return (int) Math.round(path.getWeight());
    }

    private static void validateDistinctStation(Station source, Station target) {
        if (source.equals(target)) {
            throw new PathNotFoundException("출발지와 도착지는 동일할 수 없습니다.");
        }
    }

    private static GraphPath<Station, DefaultWeightedEdge> generatePath(List<Section> sections, Station source, Station target) {
        try {
            WeightedMultigraph<Station, DefaultWeightedEdge> graph = generateGraph(sections);
            return new DijkstraShortestPath<>(graph).getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new PathNotFoundException("노선에 등록되지 않은 역의 경로는 조회할 수 없습니다.");
        }
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> generateGraph(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance().getValue()
            );
        }

        return graph;
    }

    private static void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new PathNotFoundException("경로가 존재하지 않습니다.");
        }
    }

}
