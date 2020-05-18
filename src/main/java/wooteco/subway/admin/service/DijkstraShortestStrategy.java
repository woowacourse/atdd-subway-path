package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.admin.domain.CustomEdge;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.dto.res.GraphResultResponse;
import wooteco.subway.admin.exception.ErrorCode;
import wooteco.subway.admin.exception.NoEdgeBetweenException;

@Component
public class DijkstraShortestStrategy extends BaseGraphStrategy {
    @Override
    protected GraphResultResponse getPathResponse(WeightedMultigraph<Long, CustomEdge> graph,
        Long source, Long target) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        final GraphPath path = dijkstraShortestPath.getPath(source, target);
        validateNoConnection(path);
        return getPathLineStation(path.getEdgeList(), path.getVertexList());
    }

    private GraphResultResponse getPathLineStation(List<CustomEdge> result, List<Long> stationIds) {
        return new GraphResultResponse(stationIds, distance(result), duration(result));
    }

    private int duration(List<CustomEdge> result) {
        return result.stream()
            .map(CustomEdge::getLineStation)
            .mapToInt(LineStation::getDuration)
            .sum();
    }

    private int distance(List<CustomEdge> result) {
        return result.stream()
            .map(CustomEdge::getLineStation)
            .mapToInt(LineStation::getDistance)
            .sum();
    }

    private void validateNoConnection(GraphPath path) {
        if (Objects.isNull(path)) {
            throw new NoEdgeBetweenException(ErrorCode.NO_EDGE);
        }
    }
}
