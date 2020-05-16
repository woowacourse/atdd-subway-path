package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.path.PathRequestWithId;
import wooteco.subway.admin.dto.path.PathType;
import wooteco.subway.admin.exception.NotFoundPathException;

import java.util.List;
import java.util.Objects;

@Service
public class GraphService {
    public List<Long> findPath(List<Line> lines, PathRequestWithId pathRequestWithId) {
        Long source = pathRequestWithId.getSourceId();
        Long target = pathRequestWithId.getTargetId();
        PathType pathType = pathRequestWithId.getPathType();

        WeightedMultigraph<Long, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream()
            .flatMap(it -> it.getLineStationsId().stream())
            .forEach(graph::addVertex);
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .filter(it -> Objects.nonNull(it.getPreStationId()))
            .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStationId(), it.getStationId()), pathType.getWeight(it)));
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        GraphPath path = dijkstraShortestPath.getPath(source, target);
        validatePath(path);
        return path.getVertexList();
    }

    private void validatePath(GraphPath path) {
        if (Objects.isNull(path)) {
            throw new NotFoundPathException();
        }
    }
}
