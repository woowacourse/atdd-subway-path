package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.CustomEdge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.GraphResultResponse;

public abstract class BaseGraphStrategy implements PathStrategy {
    @Override
    public GraphResultResponse getPath(List<Line> lines, Long source, Long target,
        CriteriaType type) {
        WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);
        lines.stream()
            .flatMap(it -> it.getLineStationsId().stream())
            .forEach(graph::addVertex);
        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .filter(it -> Objects.nonNull(it.getPreStationId()))
            .forEach(it -> graph.addEdge(it.getPreStationId(), it.getStationId(),
                new CustomEdge(it, type)));

        return getPathResponse(graph, source, target);
    }

    protected abstract GraphResultResponse getPathResponse(
        WeightedMultigraph<Long, CustomEdge> graph, Long source, Long target);
}
