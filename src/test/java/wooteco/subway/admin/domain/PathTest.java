package wooteco.subway.admin.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.admin.dto.ShortestPath;

public class PathTest {
	private Lines lines;
	private List<Line> lineSources = new ArrayList<>();
	private Line line1;

	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;
	private List<Station> sourceStations;
	private Stations stations;

	@BeforeEach
	void setUp() {
		line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-500");
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 10, 10));
		line1.addLineStation(new LineStation(2L, 3L, 10, 10));
		line1.addLineStation(new LineStation(3L, 4L, 10, 10));
		lineSources.add(line1);

		lines = new Lines(lineSources);

		station1 = new Station(1L, "구로");
		station2 = new Station(2L, "신도림");
		station3 = new Station(3L, "신길");
		station4 = new Station(4L, "용산");
		sourceStations = Arrays.asList(station1, station2, station3, station4);
		stations = new Stations(sourceStations);

		// TODO: 2020/05/15 빈약한 테스트 케이스를 보강하렴^^
	}

	@DisplayName("주어진 Criteria에 대한 ShortestPath를 반환")
	@ValueSource(strings = {"distance", "duration"})
	@ParameterizedTest
	void findShortestPath(String criteria) {
		Path path = new Path(lines, stations);


		ShortestPath shortestPath = path.findShortestPath(station1, station4, Criteria.of(criteria));

		assertEquals(shortestPath.getPath().get(0), station1);
		assertEquals(shortestPath.getPath().get(1), station2);
		assertEquals(shortestPath.getPath().get(2), station3);
		assertEquals(shortestPath.getPath().get(3), station4);
	}
}
