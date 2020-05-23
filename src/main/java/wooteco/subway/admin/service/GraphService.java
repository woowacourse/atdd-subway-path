package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class GraphService {

    public List<Long> findShortestPath(Long source, Long target, PathType type, List<Line> lines) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.stream()
                .flatMap(line -> line.getLineStationsId().stream())
                .forEach(graph::addVertex);

        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .forEach(station -> {
                    graph.setEdgeWeight(graph.addEdge(station.getPreStationId(), station.getStationId()), type.getWeight(station));
                });

        return findShortestPathByDijkstra(source, target, graph);
    }

    private List<Long> findShortestPathByDijkstra(Long source, Long target, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        List<Long> shortestPath;
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        try {
            shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }
        return shortestPath;
    }

}
