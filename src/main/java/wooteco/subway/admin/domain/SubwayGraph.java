package wooteco.subway.admin.domain;

import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SubwayGraph {
    private WeightedMultigraph<Long, LineStationEdge> graph;

    public SubwayGraph() {
        this.graph = new WeightedMultigraph<>(LineStationEdge.class);
    }

    public WeightedMultigraph<Long, LineStationEdge> makeGraph(List<Line> lines, Map<Long, Station> stations, PathType pathType) {
        stations.values()
                .forEach(station -> graph.addVertex(station.getId()));

        lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .forEach(lineStation -> addEdge(lineStation, graph, pathType));
        return graph;
    }

    private void addEdge(LineStation lineStation, WeightedMultigraph<Long, LineStationEdge> graph, PathType pathType) {
        LineStationEdge edge = LineStationEdge.of(lineStation);
        graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), edge);
        graph.setEdgeWeight(edge, pathType.getWeight(lineStation));
    }
}
