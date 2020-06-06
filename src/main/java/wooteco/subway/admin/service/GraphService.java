package wooteco.subway.admin.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;

import java.util.List;
import java.util.Objects;

@Service
public class GraphService {

    private ShortestPathService shortestPathService;

    public GraphService(ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    public List<Long> findPath(List<Line> lines, Long source, Long target, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.stream()
                .flatMap(it -> it.getLineStationsId().stream())
                .forEach(it -> graph.addVertex(it));

        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStationId(), it.getStationId()), type.findWeightOf(it)));

        return shortestPathService.getPath(source, target, graph);
    }
}