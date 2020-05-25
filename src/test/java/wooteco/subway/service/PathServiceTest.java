package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Graph;
import wooteco.subway.domain.path.Graphs;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
	private static final String 강남 = "강남역";
	private static final String 역삼 = "역삼역";
	private static final String 선릉 = "선릉역";
	private static final String 삼성 = "삼성역";
	private static final String 양재 = "양재역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private PathService pathService;

	private Line line;
	private Line line4;
	private Station 강남역;
	private Station 역삼역;
	private Station 선릉역;
	private Station 삼성역;
	private Station 양재역;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineRepository, stationRepository);

		강남역 = new Station(1L, 강남);
		역삼역 = new Station(2L, 역삼);
		선릉역 = new Station(3L, 선릉);
		삼성역 = new Station(4L, 삼성);
		양재역 = new Station(5L, 양재);

		line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
		line.addLineStation(new LineStation(null, 1L, 0, 10));
		line.addLineStation(new LineStation(1L, 2L, 10, 1));
		line.addLineStation(new LineStation(2L, 3L, 10, 1));
		line.addLineStation(new LineStation(3L, 4L, 10, 1));

		line4 = new Line(2L, "4호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-blue-600");
		line4.addLineStation(new LineStation(1L, 5L, 10, 10));
		line4.addLineStation(new LineStation(5L, 4L, 10, 10));



	}

	@DisplayName("경로 조회시, 최단 경로 기준으로 path생성")
	@Test
	void searchPath_GivenDistanceWeight_CreatePath() {
		// given
		List<Station> stations = Arrays.asList(강남역, 역삼역, 선릉역, 삼성역, 양재역);
		List<Line> lines = Arrays.asList(line, line4);
		Graphs.getInstance().create(lines, stations);

		List<StationResponse> expected = StationResponse.listOf(
			Arrays.asList(강남역, 양재역, 삼성역));

		//when
		PathResponse pathResponse = pathService.searchPath(강남, 삼성,
			PathType.DISTANCE.name());

		//then
		List<StationResponse> actual = pathResponse.getStations();
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("경로 조회시, 최단 시간을 기준으로 path생성")
	@Test
	void searchPath_GivenDurationWeight_CreatePath() {
		// given
		List<Station> stations = Arrays.asList(강남역, 역삼역, 선릉역, 삼성역, 양재역);
		Graphs.getInstance().create(Collections.singletonList(line), stations);

		List<StationResponse> expected = StationResponse.listOf(Arrays.asList(강남역, 역삼역, 선릉역, 삼성역));

		//when
		PathResponse pathResponse = pathService.searchPath(강남, 삼성, PathType.DURATION.name());

		//then
		List<StationResponse> actual = pathResponse.getStations();
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("예외테스트: 경로 조회시, 출발역과 도착역이 같은 경우 예외 발생 확인")
	@Test
	void searchPath_GivenSameStations_ExceptionThrown() {
		assertThatThrownBy(
			() -> pathService.searchPath(강남, 강남, PathType.DISTANCE.name()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("출발역과 도착역은 같을 수 없습니다");
	}
}
