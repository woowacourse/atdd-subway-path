package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.admin.domain.PathSearchType.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.NoSuchStationException;
import wooteco.subway.admin.exception.NullStationIdException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;

class PathTest {
    private Path path;
    private Line lineOne;
    private Line lineTwo;

    private static Stream<Arguments> distancePathSets() {
        return Stream.of(
            Arguments.of(9, Arrays.asList(1L, 2L, 3L, 4L)),
            Arguments.of(2, Arrays.asList(1L, 2L, 4L))
        );
    }

    private static Stream<Arguments> durationPathSets() {
        return Stream.of(
            Arguments.of(1, Arrays.asList(1L, 2L, 4L)),
            Arguments.of(10, Arrays.asList(1L, 2L, 3L, 4L))
        );
    }

    private static Stream<Arguments> wholeDistanceForShortestDistancePath() {
        return Stream.of(
            Arguments.of(9, 13),
            Arguments.of(2, 7)
        );
    }

    private static Stream<Arguments> wholeDistanceForShortestDurationPath() {
        return Stream.of(
            Arguments.of(1, 14),
            Arguments.of(10, 13)
        );
    }

    private static Stream<Arguments> wholeDurationForShortestDistancePath() {
        return Stream.of(
            Arguments.of(9, 9),
            Arguments.of(2, 4)
        );
    }

    private static Stream<Arguments> wholeDurationForShortestDurationPath() {
        return Stream.of(
            Arguments.of(1, 4),
            Arguments.of(10, 9)
        );
    }

    private static Stream<Arguments> sourceAndTargetIncludeNull() {
        return Stream.of(
            Arguments.of(null, 1L, DISTANCE),
            Arguments.of(1L, null, DISTANCE),
            Arguments.of(null, null, DISTANCE),
            Arguments.of(null, 1L, DURATION),
            Arguments.of(1L, null, DURATION),
            Arguments.of(null, null, DURATION)
        );
    }

    @BeforeEach
    void setUp() {
        lineOne = new Line(1L, "1호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
        lineTwo = new Line(2L, "2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);

        lineOne.addLineStation(new LineStation(null, 1L, 0, 0));
        lineOne.addLineStation(new LineStation(1L, 2L, 5, 3));
        lineOne.addLineStation(new LineStation(2L, 3L, 5, 2));
        lineOne.addLineStation(new LineStation(3L, 4L, 3, 4));
    }

    @DisplayName("최단거리 경로속에 포함되는 역의 ID를 순서대로 정확히 반환한다.")
    @ParameterizedTest
    @MethodSource("distancePathSets")
    void findShortestPath(int distance, List<Long> shortestPath) {
        lineTwo.addLineStation(new LineStation(null, 4L, 0, 10));
        lineTwo.addLineStation(new LineStation(4L, 2L, distance, 10));
        lineTwo.addLineStation(new LineStation(2L, 5L, 3, 10));

        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThat(path.findShortestPath(1L, 4L, DISTANCE)).isEqualTo(shortestPath);
    }

    @DisplayName("최단시간 경로속에 포함되는 역의 ID를 순서대로 정확히 반환한다.")
    @ParameterizedTest
    @MethodSource("durationPathSets")
    void findShortestDurationPath(int duration, List<Long> shortestPath) {
        lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
        lineTwo.addLineStation(new LineStation(4L, 2L, 9, duration));
        lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThat(path.findShortestPath(1L, 4L, DURATION)).isEqualTo(shortestPath);
    }

    @DisplayName("최단거리 경로 이동시 소요 거리를 정확히 반환한다.")
    @ParameterizedTest
    @MethodSource("wholeDistanceForShortestDistancePath")
    void calculateDistance(int distance, int expected) {
        lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
        lineTwo.addLineStation(new LineStation(4L, 2L, distance, 1));
        lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThat(path.calculateShortestDistance(1L, 4L)).isEqualTo(expected);
    }

    @DisplayName("최단시간 경로를 이동하는데 걸리는 총 거리를 정확히 반환한다.")
    @ParameterizedTest
    @MethodSource("wholeDistanceForShortestDurationPath")
    void calculateDistanceForShortestDurationPath(int duration, int expected) {
        lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
        lineTwo.addLineStation(new LineStation(4L, 2L, 9, duration));
        lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThat(path.calculateDistanceForShortestDurationPath(1L, 4L)).isEqualTo(expected);
    }

    @DisplayName("최단거리 경로 이동시 소요 시간을 정확히 반환한다.")
    @ParameterizedTest
    @MethodSource("wholeDurationForShortestDistancePath")
    void calculateDurationFor(int distance, int expected) {
        lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
        lineTwo.addLineStation(new LineStation(4L, 2L, distance, 1));
        lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThat(path.calculateDurationForShortestDistancePath(1L, 4L)).isEqualTo(expected);
    }

    @DisplayName("최단시간 경로를 이동하는데 걸리는 총 소요시간을 정확히 반환한다.")
    @ParameterizedTest
    @MethodSource("wholeDurationForShortestDurationPath")
    void calculateDuration(int duration, int expected) {
        lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
        lineTwo.addLineStation(new LineStation(4L, 2L, 9, duration));
        lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThat(path.calculateShortestDuration(1L, 4L)).isEqualTo(expected);
    }

    @DisplayName("최단 경로/시간 조회시 출발역과 도착역이 같은 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"DISTANCE", "DURATION"})
    void findShortestPathWithSameSourceAndDestination(PathSearchType type) {
        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThatThrownBy(() -> path.findShortestPath(1L, 1L, type))
            .isInstanceOf(SameSourceAndDestinationException.class);
    }

    @DisplayName("최단 경로/시간 조회시 출발역과 도착역이 연결되어있지 않은 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"DISTANCE", "DURATION"})
    void findShortestPathBetweenDisconnectedSourceAndDestination(PathSearchType type) {
        lineTwo.addLineStation(new LineStation(null, 100L, 3, 10));
        path = new Path(Arrays.asList(lineOne, lineTwo));
        assertThatThrownBy(() -> path.findShortestPath(1L, 100L, type))
            .isInstanceOf(DisconnectedPathException.class);
    }

    @DisplayName("최단 경로 조회시 출발역이나 도착역이 없는 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"DISTANCE,100,1", "DISTANCE,1,100", "DURATION,100,1", "DURATION,1,100"})
    void findShortestDurationPathWithNonExistentSourceOrDestination(PathSearchType type,
        Long source, Long target) {
        path = new Path(Collections.singletonList(lineOne));
        assertThatThrownBy(() -> path.findShortestPath(source, target, type))
            .isInstanceOf(NoSuchStationException.class);
    }

    @DisplayName("최단 시간/경로 조회시 출발역이나 도착역에 null이 포함된 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @MethodSource("sourceAndTargetIncludeNull")
    void findShortestPathWithNullSourceOrDestination(Long source, Long target,
        PathSearchType type) {
        path = new Path(Collections.singletonList(lineOne));
        assertThatThrownBy(() -> path.findShortestPath(source, target, type)).isInstanceOf(
            NullStationIdException.class);
    }
}
