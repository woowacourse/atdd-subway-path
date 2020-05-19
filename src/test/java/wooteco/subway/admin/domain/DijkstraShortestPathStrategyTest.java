package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.admin.domain.PathType.DISTANCE;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import wooteco.subway.admin.dto.FindPathRequest;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.exception.VerticesNotConnectedException;

public class DijkstraShortestPathStrategyTest {
    DijkstraShortestPathStrategy dijkstraShortestPathStrategy = new DijkstraShortestPathStrategy();
    private Lines lines;
    private Stations stations;

    @BeforeEach
    private void setUp() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 10);
        Line line = new Line(1L, "1호선", LocalTime.now(), LocalTime.now(), 10, "bg-red-500");
        line.addLineStation(lineStation);
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "강남");
        Station station3 = new Station(3L, "양재시민의숲");

        lines = new Lines(Collections.singletonList(line));
        stations = new Stations(Arrays.asList(station1, station2, station3));
    }

    @EnumSource(value = PathType.class, names = {"DISTANCE", "DURATION"})
    @ParameterizedTest
    void findShortestPath(PathType pathType) {
        assertThat(dijkstraShortestPathStrategy.findShortestPath(lines, stations,
                new FindPathRequest("잠실", "강남", pathType.name())))
                .isInstanceOf(SubwayPath.class);
    }

    @Test
    void sameStationNameTest() {
        assertThatThrownBy(() ->
                dijkstraShortestPathStrategy.findShortestPath(lines, stations,
                        new FindPathRequest("잠실", "잠실", DISTANCE.name()))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void notConnectedGraph() {
        assertThatThrownBy(() ->
                dijkstraShortestPathStrategy.findShortestPath(lines, stations,
                        new FindPathRequest("잠실", "양재시민의숲", DISTANCE.name()))
        ).isInstanceOf(VerticesNotConnectedException.class);
    }

    @Test
    void stationNameNotFound() {
        assertThatThrownBy(() ->
                dijkstraShortestPathStrategy.findShortestPath(lines, stations,
                        new FindPathRequest("잠실", "포비", DISTANCE.name()))
        ).isInstanceOf(StationNotFoundException.class);
    }
}
