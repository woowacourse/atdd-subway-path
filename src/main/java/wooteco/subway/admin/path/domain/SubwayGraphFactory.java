package wooteco.subway.admin.path.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.station.domain.Station;

public class SubwayGraphFactory {

    public static WeightedMultigraph<Station, SubwayWeightedEdge> createGraphBy(final ShortestPathType pathType,
        final Map<Long, Station> stations, final List<LineStation> lineStations) {
        final WeightedMultigraph<Station, SubwayWeightedEdge> graph =
            new WeightedMultigraph<>(SubwayWeightedEdge.class);

        stations.values().forEach(graph::addVertex);

        lineStations.stream()
                    .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                    .forEach(lineStation -> pathType.setEdgeByPathType(graph, stations, lineStation));

        return graph;
    }

}
