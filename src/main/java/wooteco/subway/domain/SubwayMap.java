package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.EmptyResultException;

import java.util.List;

public class SubwayMap {
    private final DijkstraShortestPath<Station, CustomEdge> pathFinder;

    private SubwayMap(DijkstraShortestPath<Station, CustomEdge> pathFinder) {
        this.pathFinder = pathFinder;
    }

    public static SubwayMap of(List<Line> lines) {
        WeightedMultigraph<Station, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);

        for (Line line : lines) {
            addVertex(graph, line.getStations());
        }

        for (Line line : lines) {
            addEdge(graph, line.getSections(), line.getId());
        }

        return new SubwayMap(new DijkstraShortestPath<Station, CustomEdge>(graph));
    }

    private static void addVertex(WeightedMultigraph<Station, CustomEdge> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(WeightedMultigraph<Station, CustomEdge> graph, List<Section> sections, Long lineId) {
        for (Section section : sections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), new CustomEdge(lineId, section.getDistance()));
        }
    }

    public Path findShortestPath(Station source, Station target) {
        GraphPath<Station, CustomEdge> path = pathFinder.getPath(source, target);

        checkNoPath(path);
        return Path.of(path.getVertexList(), path.getWeight(), path.getEdgeList());
    }

    private void checkNoPath(GraphPath<Station, CustomEdge> path) {
        if (path == null) {
            throw new EmptyResultException("출발역과 도착역 사이에 연결된 경로가 없습니다.");
        }
    }
}
