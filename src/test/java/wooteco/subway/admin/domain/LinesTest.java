package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LinesTest {
	private Lines lines;
	private Line lineOne;
	private Line lineTwo;

	private static Stream<Arguments> distancePathSets() {
		return Stream.of(
			Arguments.of(9, Arrays.asList(1L, 2L, 3L, 4L)),
			Arguments.of(2, Arrays.asList(1L, 2L, 4L))
		);
	}

	private static Stream<Arguments> distance() {
		return Stream.of(
			Arguments.of(9, 13),
			Arguments.of(2, 7)
		);
	}

	private static Stream<Arguments> duration() {
		return Stream.of(
			Arguments.of(9, 30),
			Arguments.of(2, 20)
		);
	}

	@BeforeEach
	void setUp() {
		lineOne = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		lineTwo = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

		lineOne.addLineStation(new LineStation(null, 1L, 0, 10));
		lineOne.addLineStation(new LineStation(1L, 2L, 5, 10));
		lineOne.addLineStation(new LineStation(2L, 3L, 5, 10));
		lineOne.addLineStation(new LineStation(3L, 4L, 3, 10));

	}

	@ParameterizedTest
	@MethodSource("distancePathSets")
	void findShortestPath(int distance, List<Long> shortestPath) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 10));
		lineTwo.addLineStation(new LineStation(4L, 2L, distance, 10));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 10));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.findShortestPath(1L, 4L)).isEqualTo(shortestPath);
	}

	@ParameterizedTest
	@MethodSource("distance")
	void calculateDistance(int distance, int expected) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 10));
		lineTwo.addLineStation(new LineStation(4L, 2L, distance, 10));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 10));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.calculateDistance(1L, 4L)).isEqualTo(expected);
	}

	@ParameterizedTest
	@MethodSource("duration")
	void calculateDuration(int distance, int expected) {
		lineTwo.addLineStation(new LineStation(null, 4L, 0, 10));
		lineTwo.addLineStation(new LineStation(4L, 2L, distance, 10));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 10));

		lines = new Lines(Arrays.asList(lineOne, lineTwo));

		assertThat(lines.calculateDuration(1L, 4L)).isEqualTo(expected);
	}

}