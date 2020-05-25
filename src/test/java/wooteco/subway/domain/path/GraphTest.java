package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

class GraphTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "삼성역";

	private List<Line> lines;
	private List<Station> stations;
	private WeightStrategy strategy;

	@BeforeEach
	void setUp() {
		Station station1 = new Station(1L, STATION_NAME1);
		Station station2 = new Station(2L, STATION_NAME2);
		Station station3 = new Station(3L, STATION_NAME3);
		Station station4 = new Station(4L, STATION_NAME4);

		Line line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
		line.addLineStation(new LineStation(null, 1L, 10, 10));
		line.addLineStation(new LineStation(1L, 2L, 10, 10));
		line.addLineStation(new LineStation(2L, 3L, 10, 10));

		Line line2 = new Line();
		line2.addLineStation(new LineStation(null, station4.getId(), 0, 0));

		lines = Arrays.asList(line, line2);
		stations = Arrays.asList(station1, station2, station3, station4);
		strategy = PathType.findPathType("DISTANCE");
	}

	@DisplayName("그래프 생성")
	@Test
	void create() {
		assertThat(new Graph(lines, stations, strategy)).isInstanceOf(Graph.class);
	}

	@DisplayName("올바른 경로 요청 시 경로 생성")
	@Test
	void createPath_GivenValidPath_ReturnPath() {
		Graph graph = new Graph(lines, stations, strategy);

		assertThat(graph.createPath(STATION_NAME1, STATION_NAME3)).isNotNull();
	}

	@DisplayName("예외테스트: 연결되지 않은 역의 경로를 요청 시, 예외 발생")
	@Test
	void createPath_GivenNotConnectedPath_ExceptionThrown() {
		Graph graph = new Graph(lines, stations, strategy);

		assertThatThrownBy(() -> graph.createPath(STATION_NAME1, STATION_NAME4))
			.isInstanceOf(InvalidPathException.class)
			.hasMessage(String.format(InvalidPathException.NOT_CONNECTED_PATH, STATION_NAME1, STATION_NAME4));
	}
}
