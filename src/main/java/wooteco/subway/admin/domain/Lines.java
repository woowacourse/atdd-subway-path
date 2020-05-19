package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.graph.WeightedMultigraph;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    private List<Long> getStationIds() {
        return lines.stream()
            .map(Line::getLineStationsId)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    public WeightedMultigraph<Long, LineStationEdge> makeGraph(PathType pathType) {
        WeightedMultigraph<Long, LineStationEdge> graph = new WeightedMultigraph<>(
            LineStationEdge.class);
        getStationIds().forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .forEach(lineStation -> makeEdge(graph, lineStation, pathType));

        return graph;
    }

    private void makeEdge(WeightedMultigraph<Long, LineStationEdge> graph, LineStation lineStation,
        PathType pathType) {
        if (!lineStation.isFirstLineStation()) {
            LineStationEdge edge = LineStationEdge.of(lineStation);
            graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), edge);
            graph.setEdgeWeight(edge, pathType.getWeight(lineStation));
        }
    }
}
