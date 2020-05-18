package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.security.InvalidParameterException;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.admin.exception.NoExistPathException;

class PathTest {
    private Path path;
    private Line line2;
    private Line line3;
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        line2 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-green-600");

        line3 = new Line(2L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5,
            "bg-orange-600");

        stations = Arrays.asList(new Station(1L, "잠실역"), new Station(3L, "선릉역"),
            new Station(2L, "삼성역"), new Station(4L, "양재역"), new Station(5L, "매봉역"),
            new Station(6L, "강변역"), new Station(7L, "서울역"));

        line2.addLineStation(new LineStation(null, 1L, 10, 5));
        line2.addLineStation(new LineStation(1L, 3L, 10, 5));
        line2.addLineStation(new LineStation(3L, 2L, 10, 5));
        line3.addLineStation(new LineStation(null, 4L, 15, 1));
        line3.addLineStation(new LineStation(4L, 5L, 15, 1));
        line3.addLineStation(new LineStation(4L, 1L, 15, 1));
        line3.addLineStation(new LineStation(5L, 6L, 15, 1));
        line3.addLineStation(new LineStation(6L, 2L, 15, 1));
        path = new Path();
        path.addVertexes(stations);
    }

    @DisplayName("최단 거리 경로 조회")
    @Test
    void getShortestDistancePath() {
        //given
        path.setEdges(Collections.singletonList(line2), PathType.DISTANCE);
        //when
        GraphPath<Long, PathEdge> pathStations = path.searchShortestPath(stations.get(0),
            stations.get(2));

        int distance = path.calculateDistance(pathStations);
        int duration = path.calculateDuration(pathStations);
        //then
        assertThat(pathStations.getVertexList().size()).isEqualTo(3);
        assertThat(pathStations.getVertexList().get(0)).isEqualTo(stations.get(0).getId());
        assertThat(pathStations.getVertexList().get(1)).isEqualTo(stations.get(1).getId());
        assertThat(pathStations.getVertexList().get(2)).isEqualTo(stations.get(2).getId());
        assertThat(distance).isEqualTo(20);
        assertThat(duration).isEqualTo(10);
    }

    @DisplayName("최단 시간 경로 조회")
    @Test
    void getShortestDurationPath() {
        //given
        path.setEdges(Collections.singletonList(line3), PathType.DURATION);
        //when
        GraphPath<Long, PathEdge> shortestPath = path.searchShortestPath(stations.get(0),
            stations.get(2));

        int distance = path.calculateDistance(shortestPath);
        int duration = path.calculateDuration(shortestPath);
        //then
        assertThat(shortestPath.getVertexList().size()).isEqualTo(4);
        assertThat(shortestPath.getVertexList().get(0)).isEqualTo(stations.get(0).getId());
        assertThat(shortestPath.getVertexList().get(1)).isEqualTo(stations.get(4).getId());
        assertThat(shortestPath.getVertexList().get(2)).isEqualTo(stations.get(5).getId());
        assertThat(shortestPath.getVertexList().get(3)).isEqualTo(stations.get(2).getId());
        assertThat(distance).isEqualTo(45);
        assertThat(duration).isEqualTo(3);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInput")
    void getShortestDistancePathWithInvalidInput(Station source, Station target, PathType pathType,
        Class exceptionClass, String message) {
        path.setEdges(Collections.singletonList(line3), pathType);
        assertThatThrownBy(() -> path.searchShortestPath(source, target))
            .isInstanceOf(exceptionClass)
            .hasMessage(message);
    }

    private static Stream<Arguments> provideInvalidInput() {
        Station station = new Station(1L, "잠실역");
        Station station2 = new Station(7L, "서울역");
        return Stream.of(
            Arguments.of(station, station, PathType.DISTANCE, InvalidParameterException.class,
                "출발역과 도착역은 같을 수 없습니다."),
            Arguments.of(station, station2, PathType.DISTANCE, NoExistPathException.class,
                "출발역과 도착역이 연결되어 있지 않습니다.")
        );
    }
}
