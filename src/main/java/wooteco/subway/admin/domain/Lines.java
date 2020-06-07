package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void addVertex(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        lines.stream()
                .flatMap(it -> it.getLineStationsId().stream())
                .forEach(graph::addVertex);
    }

    public void setEdgeWeight(WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathType type) {
        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStationId(), it.getStationId()), type.findWeightOf(it)));
    }

    public void addLineStationsWithOutSourceLineStation(List<LineStation> lineStations) {
        lines.stream()
                .map(Line::lineStationsWithOutSourceLineStation)
                .forEach(lineStations::addAll);
    }
}
