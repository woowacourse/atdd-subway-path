package wooteco.subway.admin.domain;

import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.response.ShortestPathResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {
	private Lines lines;
	private List<Line> tempLines = new ArrayList<>();
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

	@BeforeEach
	void setUp() {
		line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-500");
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 3, 10));
		line1.addLineStation(new LineStation(2L, 3L, 3, 10));
		line1.addLineStation(new LineStation(3L, 4L, 3, 10));
		tempLines.add(line1);

		line2 = new Line(1L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-red-500");
		line2.addLineStation(new LineStation(null, 2L, 10, 10));
		line2.addLineStation(new LineStation(2L, 5L, 10, 3));
		line2.addLineStation(new LineStation(5L, 6L, 10, 3));
		line2.addLineStation(new LineStation(6L, 7L, 10, 3));
		line2.addLineStation(new LineStation(7L, 8L, 10, 3));
		line2.addLineStation(new LineStation(8L, 4L, 10, 3));
		tempLines.add(line2);

		lines = new Lines(tempLines);

		station1 = new Station(1L, "구로");
		station2 = new Station(2L, "신도림");
		station3 = new Station(3L, "신길");
		station4 = new Station(4L, "용산");
		station5 = new Station(5L, "동작");
		station6 = new Station(6L, "흑석");
		station7 = new Station(7L, "노량진");
		station8 = new Station(8L, "화곡");
		sourceStations = Arrays.asList(station1, station2, station3, station4, station5, station6, station7, station8);
		stations = new Stations(sourceStations);
	}

	@DisplayName("최단거리 경로를 반환")
	@Test
	void findShortestDistancePath() {
		Path path = new Path(new WeightedMultigraph<>(Edge.class), lines, stations, Criteria.of("distance"));

		ShortestPathResponse shortestPath = path.findShortestPath(station1, station4);

		assertEquals(shortestPath.getPath().get(0), station1);
		assertEquals(shortestPath.getPath().get(1), station2);
		assertEquals(shortestPath.getPath().get(2), station3);
		assertEquals(shortestPath.getPath().get(3), station4);
	}

	@DisplayName("최단시간 경로를 반환")
	@Test
	void findShortestDurationPath() {
		Path path = new Path(new WeightedMultigraph<>(Edge.class), lines, stations, Criteria.of("duration"));

		ShortestPathResponse shortestPath = path.findShortestPath(station1, station4);

		assertEquals(shortestPath.getPath().get(0), station1);
		assertEquals(shortestPath.getPath().get(1), station2);
		assertEquals(shortestPath.getPath().get(2), station5);
		assertEquals(shortestPath.getPath().get(3), station6);
		assertEquals(shortestPath.getPath().get(4), station7);
		assertEquals(shortestPath.getPath().get(5), station8);
		assertEquals(shortestPath.getPath().get(6), station4);
	}
}
