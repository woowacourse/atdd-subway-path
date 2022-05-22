package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private final List<Section> values;
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(List<Section> stations) {
        this.values = stations;
        this.dijkstraShortestPath = initDijkstraShortestPath();
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> initDijkstraShortestPath() {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        fillVertexesAndEdges(graph, values);
        return new DijkstraShortestPath<>(graph);
    }

    private void fillVertexesAndEdges(WeightedMultigraph<Long, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();

            graph.addVertex(upStationId);
            graph.addVertex(downStationId);

            DefaultWeightedEdge edge = graph.addEdge(upStationId, downStationId);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    public List<Long> findPath(Long from, Long to) {
        validateNotSameFromAndTo(from, to);
        validateNotConnectedStation(from, to);
        try {
            return dijkstraShortestPath.getPath(from, to).getVertexList();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("해당 경로는 이동할 수 없습니다.");
        }
    }

    public int findDistance(Long from, Long to) {
        validateNotSameFromAndTo(from, to);
        validateNotConnectedStation(from, to);
        int pathWeight = (int) dijkstraShortestPath.getPathWeight(from, to);
        if (pathWeight == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("해당 경로는 이동할 수 없습니다.");
        }
        return pathWeight;
    }

    private void validateNotSameFromAndTo(Long from, Long to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("출발지와 목적지는 서로 달라야 합니다.");
        }
    }

    private void validateNotConnectedStation(Long from, Long to) {
        boolean isFromStationConnected = values.stream()
                .noneMatch(v -> v.isUpStation(from) || v.isDownStation(from));
        boolean isToStationConnected = values.stream()
                .noneMatch(v -> v.isUpStation(to) || v.isDownStation(to));
        if (isFromStationConnected || isToStationConnected) {
            throw new IllegalArgumentException("어떠한 노선에도 등록되지 않은 역이 존재합니다.");
        }
    }
}
