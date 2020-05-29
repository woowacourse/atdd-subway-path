package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.exception.WrongPathException;

import java.util.List;
import java.util.Objects;

@Service
public class GraphService {
    public List<Long> createShortestPath(Lines lines, Long source, Long target, PathType type) {
        try {
            return createDijkstraShortestPathByLines(lines, type).getPath(source, target).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new WrongPathException();
        }
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> createDijkstraShortestPathByLines(Lines lines, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Long id : lines.getLineStationsId()) {
            graph.addVertex(id);
        }
        for (LineStation lineStation : lines.getLineStations()) {
            if (Objects.nonNull(lineStation.getPreStationId())) {
                graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()), type.getWeight(lineStation));
            }
        }
        return new DijkstraShortestPath<>(graph);
    }
}
