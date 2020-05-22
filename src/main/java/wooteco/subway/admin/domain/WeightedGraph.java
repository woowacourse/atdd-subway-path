package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.graph.WeightedMultigraph;

public class WeightedGraph {
    private WeightedMultigraph<Long, CustomEdge> graph;

    public WeightedGraph(List<Line> lines, CriteriaType type) {
        this.graph = new WeightedMultigraph<>(CustomEdge.class);

        lines.stream()
            .flatMap(it -> it.getLineStationsId().stream())
            .forEach(graph::addVertex);

        lines.stream()
            .flatMap(it -> it.getStations().stream())
            .filter(it -> Objects.nonNull(it.getPreStationId()))
            .forEach(
                it -> graph.addEdge(it.getPreStationId(), it.getStationId(),
                    new CustomEdge(it, type)
                ));
    }

    public DijkstraSPath generateDijkstra() {
        return new DijkstraSPath(this.graph);
    }
}
