package wooteco.subway.path.application;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import wooteco.subway.exceptions.SubWayException;
import wooteco.subway.exceptions.SubWayExceptionSet;
import wooteco.subway.path.dto.PathDto;
import wooteco.subway.path.dto.PathRequest;

@Component
public class DijkstraAlgorithm implements PathAlgorithms {

    @Override
    public PathDto findPath(WeightedMultigraph<Long, DefaultWeightedEdge> graph,
        PathRequest pathRequest) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(graph);
        Long sourceId = pathRequest.getSource();
        Long targetId = pathRequest.getTarget();
        try {
            List<Long> paths = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
            int distance = (int) dijkstraShortestPath.getPath(sourceId, targetId).getWeight();
            return new PathDto(paths, distance);
        } catch (NullPointerException e) {
            throw new SubWayException(SubWayExceptionSet.NOT_CONNECT_STATION_EXCEPTION);
        }
    }
}
