package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPath {
    private final WeightedMultigraph<Long, ShortestPathEdge> graph
            = new WeightedMultigraph<>(ShortestPathEdge.class);

    public ShortestPath(List<Station> stations, List<Section> sections) {
        addVertex(stations);
        addEdge(sections);
    }

    private void addVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
    }

    private void addEdge(List<Section> sections) {
        for (Section section : sections) {
            ShortestPathEdge shortestPathEdge = new ShortestPathEdge(section.getLineId(), section.getDistance());
            graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId(), shortestPathEdge);
        }
    }

    public List<Long> findStationIds(Long upStationId, Long downStationId) {
        GraphPath<Long, ShortestPathEdge> graphPath = findGraphPath(upStationId, downStationId);
        return graphPath.getVertexList();
    }

    public int findShortestDistance(Long upStationId, Long downStationId) {
        GraphPath<Long, ShortestPathEdge> graphPath = findGraphPath(upStationId, downStationId);
        return (int) graphPath.getWeight();
    }

    public List<Long> findPassedLineIds(Long upStationId, Long downStationId) {
        GraphPath<Long, ShortestPathEdge> graphPath = findGraphPath(upStationId, downStationId);

        return graphPath.getEdgeList().stream()
                .map(ShortestPathEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }

    private GraphPath<Long, ShortestPathEdge> findGraphPath(Long upStationId, Long downStationId) {
        DijkstraShortestPath<Long, ShortestPathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Long, ShortestPathEdge> path = dijkstraShortestPath.getPath(upStationId, downStationId);
        validatePathExist(path);

        return path;
    }

    private void validatePathExist(GraphPath<Long, ShortestPathEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }
}
