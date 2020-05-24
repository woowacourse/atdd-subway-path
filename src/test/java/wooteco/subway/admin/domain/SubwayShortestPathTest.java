package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.common.exception.InvalidSubwayPathException;
import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.path.domain.ShortestPathType;
import wooteco.subway.admin.path.domain.SubwayGraphFactory;
import wooteco.subway.admin.path.domain.SubwayShortestPath;
import wooteco.subway.admin.path.domain.SubwayWeightedEdge;
import wooteco.subway.admin.station.domain.Station;

class SubwayShortestPathTest {

    private Map<Long, Station> stations;
    private WeightedMultigraph<Station, SubwayWeightedEdge> shortestDistanceGraph;
    private WeightedMultigraph<Station, SubwayWeightedEdge> shortestDurationGraph;

    @BeforeEach
    void setUp() {
        Station station1 = new Station(1L, "교대");
        Station station2 = new Station(2L, "강남");
        Station station3 = new Station(3L, "양재");
        Station station4 = new Station(4L, "남부터미널");
        Station station5 = new Station(5L, "연결X");
        Station station6 = new Station(6L, "연결X");

        stations = Stream.of(station1, station2, station3, station4, station5, station6)
                         .collect(Collectors.toMap(Station::getId, station -> station));

        List<LineStation> lineStations = new ArrayList<>();
        lineStations.add(new LineStation(null, station1.getId(), 0, 0));
        lineStations.add(new LineStation(station1.getId(), station2.getId(), 3, 1));
        lineStations.add(new LineStation(null, station1.getId(), 0, 0));
        lineStations.add(new LineStation(station1.getId(), station4.getId(), 2, 1));
        lineStations.add(new LineStation(station4.getId(), station3.getId(), 2, 2));
        lineStations.add(new LineStation(null, station2.getId(), 0, 0));
        lineStations.add(new LineStation(station2.getId(), station3.getId(), 2, 1));

        shortestDistanceGraph = SubwayGraphFactory.createGraphBy(ShortestPathType.DISTANCE, stations, lineStations);
        shortestDurationGraph = SubwayGraphFactory.createGraphBy(ShortestPathType.DURATION, stations, lineStations);
    }

    @DisplayName("서로 다른 2개의 역이 이어져있는 경우 - 최단 거리")
    @Test
    void getPathStations_ByDistance() {
        final Station source = stations.get(1L);
        final Station target = stations.get(3L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDistanceGraph, source, target));

        assertThat(subwayShortestPath.getPathStations()).hasSize(3);
        assertThat(subwayShortestPath.getWeight()).isEqualTo(4);
        assertThat(subwayShortestPath.getSubWeight()).isEqualTo(3);
    }

    @DisplayName("서로 다른 2개의 역이 이어져있는 경우 - 최소 시간")
    @Test
    void getPathStations_ByDuration() {
        final Station source = stations.get(1L);
        final Station target = stations.get(3L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDurationGraph, source, target));

        assertThat(subwayShortestPath.getPathStations()).hasSize(3);
        assertThat(subwayShortestPath.getWeight()).isEqualTo(2);
        assertThat(subwayShortestPath.getSubWeight()).isEqualTo(5);
    }

    @DisplayName("source역이 없는 경우 - 최단 거리")
    @Test
    void getPathStations_ByDistance_sourceNotExists() {
        final Station source = stations.get(5L);
        final Station target = stations.get(3L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDistanceGraph, source, target));

        assertThatThrownBy(subwayShortestPath::getPathStations)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getSubWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
    }

    @DisplayName("source역이 없는 경우 - 최소 시간")
    @Test
    void getPathStations_ByDuration_sourceNotExists() {
        final Station source = stations.get(5L);
        final Station target = stations.get(3L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDurationGraph, source, target));

        assertThatThrownBy(subwayShortestPath::getPathStations)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(
            subwayShortestPath::getSubWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
    }

    @DisplayName("target역이 없는 경우 - 최단 거리")
    @Test
    void getPathStations_ByDistance_targetNotExists() {
        final Station source = stations.get(1L);
        final Station target = stations.get(5L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDistanceGraph, source, target));

        assertThatThrownBy(subwayShortestPath::getPathStations)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(
            subwayShortestPath::getSubWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
    }

    @DisplayName("target역이 없는 경우 - 최소 시간")
    @Test
    void getPathStations_ByDuration_targetNotExists() {
        final Station source = stations.get(1L);
        final Station target = stations.get(5L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDurationGraph, source, target));

        assertThatThrownBy(subwayShortestPath::getPathStations)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getSubWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
    }

    @DisplayName("target역과 source역이 모두 없는 경우 - 최단 거리")
    @Test
    void getPathStations_ByDistance_sourceAndTargetNotExists() {
        final Station source = stations.get(5L);
        final Station target = stations.get(6L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDistanceGraph, source, target));

        assertThatThrownBy(subwayShortestPath::getPathStations)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getSubWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
    }

    @DisplayName("target역과 source역이 모두 없는 경우 - 최소 시간")
    @Test
    void getPathStations_ByDuration_sourceAndTargetNotExists() {
        final Station source = stations.get(5L);
        final Station target = stations.get(6L);
        final SubwayShortestPath subwayShortestPath =
            new SubwayShortestPath(DijkstraShortestPath.findPathBetween(shortestDurationGraph, source, target));

        assertThatThrownBy(subwayShortestPath::getPathStations)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
        assertThatThrownBy(subwayShortestPath::getSubWeight)
            .isInstanceOf(InvalidSubwayPathException.class);
    }
}
