package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class LinesTest {
	private Lines lines;
	private Line lineOne;
	private Line lineTwo;

	@BeforeEach
	void setUp() {
		lineOne = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		lineTwo = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

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

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.findShortestPath(1L, 4L, PathSearchType.DISTANCE)).isEqualTo(shortestPath);
	}

	private static Stream<Arguments> distancePathSets() {
		return Stream.of(
			Arguments.of(9, Arrays.asList(1L, 2L, 3L, 4L)),
			Arguments.of(2, Arrays.asList(1L, 2L, 4L))
		);
	}

	@DisplayName("최단거리 경로를 이동하는데 걸리는 총 거리를 정확히 반환한다.")
	@ParameterizedTest
	@MethodSource("wholeDistanceForShortestDistancePath")
	void calculateDistance(int distance, int expected) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
		lineTwo.addLineStation(new LineStation(4L, 2L, distance, 1));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.calculateShortestDistance(1L, 4L)).isEqualTo(expected);
	}

	private static Stream<Arguments> wholeDistanceForShortestDistancePath() {
		return Stream.of(
			Arguments.of(9, 13),
			Arguments.of(2, 7)
		);
	}

	@DisplayName("최단 경로를 이동하는데 걸리는 총 소요시간을 정확히 반환한다.")
	@ParameterizedTest
	@MethodSource("wholeDurationForShortestDistancePath")
	void calculateDurationFor(int distance, int expected) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
		lineTwo.addLineStation(new LineStation(4L, 2L, distance, 1));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.calculateDurationForShortestDistancePath(1L, 4L)).isEqualTo(expected);
	}

	private static Stream<Arguments> wholeDurationForShortestDistancePath() {
		return Stream.of(
			Arguments.of(9, 9),
			Arguments.of(2, 4)
		);
	}

	@DisplayName("최단 경로 조회시 출발역과 도착역이 같은 경우, 예외를 발생시킨다.")
	@Test
	void findShortestPathWithSameSourceAndDestination() {
		lines = new Lines(Arrays.asList(lineOne, lineTwo));
		assertThatThrownBy(() -> lines.findShortestPath(1L, 1L, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("최단 경로 조회시 출발역과 도착역이 연결되어있지 않은 경우, 예외를 발생시킨다.")
	@Test
	void findShortestPathBetweenDisconnectedSourceAndDestination() {
		lineTwo.addLineStation(new LineStation(null, 100L, 3, 10));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));
		assertThatThrownBy(() -> lines.findShortestPath(1L, 100L, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("최단 경로 조회시 출발역이나 도착역이 없는 경우, 예외를 발생시킨다.")
	@ParameterizedTest
	@CsvSource({"100,1", "1,100", "100,100"})
	void findShortestPathWithNonExistentSourceOrDestination(Long source, Long target) {
		lines = new Lines(Collections.singletonList(lineOne));
		assertThatThrownBy(() -> lines.findShortestPath(source, target, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("최단 경로 조회시 출발역이나 도착역이 없는 경우, 예외를 발생시킨다.")
	@ParameterizedTest
	@MethodSource("sourceAndTargetIncludeNull")
	void findShortestPathWithNullSourceOrDestination(Long source, Long target) {
		lines = new Lines(Collections.singletonList(lineOne));
		assertThatThrownBy(() -> lines.findShortestPath(source, target, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	private static Stream<Arguments> sourceAndTargetIncludeNull() {
		return Stream.of(
			Arguments.of(null, 1L),
			Arguments.of(1L, null),
			Arguments.of(null, null)
		);
	}

	@DisplayName("최단시간 경로속에 포함되는 역의 ID를 순서대로 정확히 반환한다.")
	@ParameterizedTest
	@MethodSource("durationPathSets")
	void findShortestDurationPath(int duration, List<Long> shortestPath) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
		lineTwo.addLineStation(new LineStation(4L, 2L, 9, duration));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.findShortestPath(1L, 4L, PathSearchType.DURATION)).isEqualTo(shortestPath);
	}

	private static Stream<Arguments> durationPathSets() {
		return Stream.of(
			Arguments.of(1, Arrays.asList(1L, 2L, 4L)),
			Arguments.of(10, Arrays.asList(1L, 2L, 3L, 4L))
		);
	}

	@DisplayName("최단시간 경로를 이동하는데 걸리는 총 거리를 정확히 반환한다.")
	@ParameterizedTest
	@MethodSource("wholeDistanceForShortestDurationPath")
	void calculateDistanceForShortestDurationPath(int duration, int expected) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
		lineTwo.addLineStation(new LineStation(4L, 2L, 9, duration));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.calculateDistanceForShortestDurationPath(1L, 4L)).isEqualTo(expected);
	}

	private static Stream<Arguments> wholeDistanceForShortestDurationPath() {
		return Stream.of(
			Arguments.of(1, 14),
			Arguments.of(10, 13)
		);
	}

	@DisplayName("최단시간 경로를 이동하는데 걸리는 총 소요시간을 정확히 반환한다.")
	@ParameterizedTest
	@MethodSource("wholeDurationForShortestDurationPath")
	void calculateDuration(int duration, int expected) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
		lineTwo.addLineStation(new LineStation(4L, 2L, 9, duration));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.calculateShortestDuration(1L, 4L)).isEqualTo(expected);
	}

	private static Stream<Arguments> wholeDurationForShortestDurationPath() {
		return Stream.of(
			Arguments.of(1, 4),
			Arguments.of(10, 9)
		);
	}

	@DisplayName("최단시간 경로 조회시 출발역과 도착역이 같은 경우, 예외를 발생시킨다.")
	@Test
	void findShortestDurationPathWithSameSourceAndDestination() {
		lines = new Lines(Arrays.asList(lineOne, lineTwo));
		assertThatThrownBy(() -> lines.findShortestPath(1L, 1L, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("최단시간 경로 조회시 출발역과 도착역이 연결되어있지 않은 경우, 예외를 발생시킨다.")
	@Test
	void findShortestDurationPathBetweenDisconnectedSourceAndDestination() {
		lineTwo.addLineStation(new LineStation(null, 100L, 3, 10));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));
		assertThatThrownBy(() -> lines.findShortestPath(1L, 100L, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("최단시간 경로 조회시 출발역이나 도착역이 없는 경우, 예외를 발생시킨다.")
	@ParameterizedTest
	@CsvSource({"100,1", "1,100", "100,100"})
	void findShortestDurationPathWithNonExistentSourceOrDestination(Long source, Long target) {
		lines = new Lines(Collections.singletonList(lineOne));
		assertThatThrownBy(() -> lines.findShortestPath(source, target, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}

	@DisplayName("최단시간 경로 조회시 출발역이나 도착역이 없는 경우, 예외를 발생시킨다.")
	@ParameterizedTest
	@MethodSource("sourceAndTargetIncludeNull")
	void findShortestDurationPathWithNullSourceOrDestination(Long source, Long target) {
		lines = new Lines(Collections.singletonList(lineOne));
		assertThatThrownBy(() -> lines.findShortestPath(source, target, PathSearchType.DISTANCE)).isInstanceOf(
			IllegalArgumentException.class);
	}
}