package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.util.StationsUtil;

public class Graph {
    private final static WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph<>(Edge.class);

    private Graph() {
    }

    public static WeightedMultigraph<Station, Edge> of(Map<Long, Station> stations, List<Line> lines,
        PathType pathType) {
        stations.values()
            .forEach(graph::addVertex);

        lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
            .forEach(lineStation -> addEdgeAndSetEdgeWeight(stations, pathType, lineStation));

        return graph;
    }

    private static void addEdgeAndSetEdgeWeight(Map<Long, Station> stations, PathType pathType,
        LineStation lineStation) {
        Station preStation = StationsUtil.findStationWithValidation(stations, lineStation.getPreStationId());
        Station currentStation = StationsUtil.findStationWithValidation(stations, lineStation.getStationId());
        Edge edge = lineStation.toEdge();

        graph.addEdge(preStation, currentStation, edge);
        graph.setEdgeWeight(edge, pathType.getValue(lineStation));
    }
}
