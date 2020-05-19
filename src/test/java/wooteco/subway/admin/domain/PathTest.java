package wooteco.subway.admin.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.subway.admin.dto.ShortestPath;

public class PathTest {
	private Lines lines;
	private List<Line> lineSources = new ArrayList<>();
	private Line line1;
	private Line line2;

	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;
	private Station station5;
	private Station station6;
	private Station station7;
	private Station station8;
	private List<Station> sourceStations;
	private Stations stations;

	private static Stream<Arguments> giveCriteriaAndResult() {
		return Stream.of(
			Arguments.of(Criteria.DISTANCE, Arrays.asList("구로", "신도림", "신길", "용산")),
			Arguments.of(Criteria.DURATION, Arrays.asList("구로", "신도림", "동작", "흑석", "노량진", "화곡", "용산"))
		);
	}

	@BeforeEach
	void setUp() {
		line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-500");
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 3, 10));
		line1.addLineStation(new LineStation(2L, 3L, 3, 10));
		line1.addLineStation(new LineStation(3L, 4L, 3, 10));
		lineSources.add(line1);

		line2 = new Line(1L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-red-500");
		line2.addLineStation(new LineStation(null, 2L, 10, 10));
		line2.addLineStation(new LineStation(2L, 5L, 10, 3));
		line2.addLineStation(new LineStation(5L, 6L, 10, 3));
		line2.addLineStation(new LineStation(6L, 7L, 10, 3));
		line2.addLineStation(new LineStation(7L, 8L, 10, 3));
		line2.addLineStation(new LineStation(8L, 4L, 10, 3));
		lineSources.add(line2);

		lines = new Lines(lineSources);

		station1 = new Station(1L, "구로");
		station2 = new Station(2L, "신도림");
		station3 = new Station(3L, "신길");
		station4 = new Station(4L, "용산");
		sourceStations = Arrays.asList(station1, station2, station3, station4);
		station5 = new Station(5L, "동작");
		station6 = new Station(6L, "흑석");
		station7 = new Station(7L, "노량진");
		station8 = new Station(8L, "화곡");
		sourceStations = Arrays.asList(station1, station2, station3, station4, station5, station6, station7, station8);
		stations = new Stations(sourceStations);
	}

	@DisplayName("주어진 Criteria에 대한 ShortestPath를 반환")
	@MethodSource("giveCriteriaAndResult")
	@ParameterizedTest
	void findShortestPath(Criteria criteria, List<String> expectedNames) {
		Path path = new Path(lines, stations, criteria);

		ShortestPath shortestPath = path.findShortestPath(station1, station4);

		List<String> stationNames = shortestPath.getPath().stream()
			.map(Station::getName)
			.collect(Collectors.toList());

		assertEquals(stationNames, expectedNames);
	}
}
