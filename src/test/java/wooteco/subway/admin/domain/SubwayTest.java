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

import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.exception.VerticesNotConnectedException;

public class SubwayTest {
    private Subway subway;

    @BeforeEach
    private void setUp() {
        LineStation lineStation = new LineStation(1L, 2L, 10, 10);
        Line line = new Line(1L, "1호선", LocalTime.now(), LocalTime.now(), 10, "bg-red-500");
        line.addLineStation(lineStation);
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "강남");
        Station station3 = new Station(3L, "양재시민의숲");

        subway = new Subway(Collections.singletonList(line), Arrays.asList(station1, station2, station3));
    }

    @EnumSource(value = PathType.class, names = {"DISTANCE", "DURATION"})
    @ParameterizedTest
    void findShortestPath(PathType pathType) {
        assertThat(subway.findShortestPath("잠실", "강남", pathType))
                .isInstanceOf(ShortestPath.class);
    }

    @Test
    void sameStationNameTest() {
        Subway subway = new Subway(Collections.emptyList(), Collections.emptyList());
        assertThatThrownBy(() -> {
            subway.findShortestPath("잠실", "잠실", DISTANCE);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void notConnectedGraph() {
        assertThatThrownBy(() -> subway.findShortestPath("잠실", "양재시민의숲", DISTANCE))
                .isInstanceOf(VerticesNotConnectedException.class);
    }

    @Test
    void stationNameNotFound() {
        assertThatThrownBy(() -> subway.findShortestPath("잠실", "포비", DISTANCE))
                .isInstanceOf(StationNotFoundException.class);
    }
}
