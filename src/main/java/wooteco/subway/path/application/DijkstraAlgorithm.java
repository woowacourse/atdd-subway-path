package wooteco.subway.path.application;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.path.dto.PathDto;
import wooteco.subway.path.dto.PathRequest;

public class DijkstraAlgorithm implements PathAlgorithms {

    @Override
    public PathDto findPath(WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathRequest pathRequest) {
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(graph);

        Long sourceId = pathRequest.getSource();
        Long targetId = pathRequest.getTarget();
        List<Long> paths = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
        int distance = (int) dijkstraShortestPath.getPath(sourceId, targetId).getWeight();

        return new PathDto(paths, distance);
    }
}
