package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.exception.NotFoundLineException;

public class Graph {

    private final WeightedMultigraph<Long, LineStationEdge> graph;

    private Graph(WeightedMultigraph<Long, LineStationEdge> graph) {
        this.graph = graph;
    }

    public static Graph of(WeightedMultigraph<Long, LineStationEdge> graph) {
        return new Graph(graph);
    }

    public static Graph of(List<Line> lines, PathType pathType) {
        return new Graph(mapLinesToGraph(lines, pathType));
    }

    private static WeightedMultigraph<Long, LineStationEdge> mapLinesToGraph(List<Line> lines,
        PathType pathType) {
        if (Objects.isNull(lines)) {
            throw new NotFoundLineException();
        }
        WeightedMultigraph<Long, LineStationEdge> graph = new WeightedMultigraph<>(
            LineStationEdge.class);
        lines.stream()
            .flatMap(it -> it.getLineStationsId().stream())
            .forEach(graph::addVertex);

        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .filter(it -> Objects.nonNull(it.getPreStationId()))
            .forEach(
                it -> graph.addEdge(it.getPreStationId(), it.getStationId(),
                    new LineStationEdge(it, pathType)));
        return graph;
    }

    public WeightedMultigraph<Long, LineStationEdge> getGraph() {
        return graph;
    }
}