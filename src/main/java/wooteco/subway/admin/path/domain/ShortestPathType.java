package wooteco.subway.admin.path.domain;

import java.util.Map;
import java.util.function.Function;

import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.station.domain.Station;

public enum ShortestPathType {

    DISTANCE(LineStation::getDistance, LineStation::getDuration),
    DURATION(LineStation::getDuration, LineStation::getDistance);

    private final Function<LineStation, Integer> weight;
    private final Function<LineStation, Integer> subWeight;

    ShortestPathType(final Function<LineStation, Integer> weight, final Function<LineStation, Integer> subWeight) {
        this.weight = weight;
        this.subWeight = subWeight;
    }

    public void setEdgeByPathType(final WeightedMultigraph<Station, SubwayWeightedEdge> graph,
        final Map<Long, Station> stations, final LineStation lineStation) {
        final Station preStation = stations.get(lineStation.getPreStationId());
        final Station station = stations.get(lineStation.getStationId());
        final SubwayWeightedEdge edge = graph.addEdge(preStation, station);

        edge.setSubWeight(this.subWeight.apply(lineStation));
        graph.setEdgeWeight(edge, this.weight.apply(lineStation));
    }

}
