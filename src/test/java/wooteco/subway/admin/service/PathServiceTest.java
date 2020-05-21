package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.ShortestPath;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.PathSearchRequest;
import wooteco.subway.admin.exception.EmptyStationNameException;
import wooteco.subway.admin.exception.NoPathExistsException;
import wooteco.subway.admin.exception.NoStationNameExistsException;
import wooteco.subway.admin.exception.SourceEqualsTargetException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
	@Mock
	private LineRepository lineRepository;

	@Mock
	private StationRepository stationRepository;

	private PathService pathService;

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
	private Station station9;
	private Station station10;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineRepository, stationRepository);

		station1 = new Station(1L, "구로");
		station2 = new Station(2L, "신도림");
		station3 = new Station(3L, "신길");
		station4 = new Station(4L, "용산");
		station5 = new Station(5L, "서울역");
		station6 = new Station(6L, "시청");
		station7 = new Station(7L, "충정로");
		station8 = new Station(8L, "당산");
		station9 = new Station(9L, "영등포구청");
		station10 = new Station(10L, "대림");

		line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-red-800");
		line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-blue-800");

		line1.addLineStation(new LineStation(null, station1.getId(), 10, 40));
		line1.addLineStation(new LineStation(station1.getId(), station2.getId(), 10, 40));
		line1.addLineStation(new LineStation(station2.getId(), station3.getId(), 10, 40));
		line1.addLineStation(new LineStation(station3.getId(), station4.getId(), 10, 40));
		line1.addLineStation(new LineStation(station4.getId(), station5.getId(), 10, 40));
		line1.addLineStation(new LineStation(station5.getId(), station6.getId(), 10, 40));

		line2.addLineStation(new LineStation(null, station6.getId(), 40, 10));
		line2.addLineStation(new LineStation(station6.getId(), station7.getId(), 40, 10));
		line2.addLineStation(new LineStation(station7.getId(), station8.getId(), 40, 10));
		line2.addLineStation(new LineStation(station8.getId(), station9.getId(), 40, 10));
		line2.addLineStation(new LineStation(station9.getId(), station2.getId(), 40, 10));
		line2.addLineStation(new LineStation(station2.getId(), station10.getId(), 40, 10));
	}

	private static Stream<Arguments> provideCriteriaAndResult() {
		Station station2 = new Station(2L, "신도림");
		Station station3 = new Station(3L, "신길");
		Station station4 = new Station(4L, "용산");
		Station station5 = new Station(5L, "서울역");
		Station station6 = new Station(6L, "시청");
		Station station7 = new Station(7L, "충정로");
		Station station8 = new Station(8L, "당산");
		Station station9 = new Station(9L, "영등포구청");

		return Stream.of(
				Arguments.of("distance", Arrays.asList(station6, station5, station4, station3, station2), 40, 160),
				Arguments.of("duration", Arrays.asList(station6, station7, station8, station9, station2), 160, 40)
		);
	}

	@DisplayName("최단 거리와 시간 경로를 조회하는 테스트")
	@ParameterizedTest
	@MethodSource("provideCriteriaAndResult")
	void getShortestPath(String criteria, List<Station> expectedPath, int expectedDistance, int expectedDuration) {
		String sourceName = "시청";
		String targetName = "신도림";

		when(stationRepository.findByName(sourceName)).thenReturn(Optional.of(station6));
		when(stationRepository.findByName(targetName)).thenReturn(Optional.of(station2));

		when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));
		when(stationRepository.findAllById(anyList())).thenReturn(Arrays.asList(station1, station2, station3, station4,
				station5, station6, station7, station8, station9, station10));

		ShortestPath shortestPath = pathService.findShortestDistancePath(new PathSearchRequest(sourceName, targetName, criteria));

		for (int i = 0; i < shortestPath.getShortestPath().size(); i++) {
			assertEquals(shortestPath.getShortestPath().get(i), expectedPath.get(i));
		}

		assertEquals(shortestPath.calculateShortestDistance(), expectedDistance);
		assertEquals(shortestPath.calculateShortestDuration(), expectedDuration);
	}

	@DisplayName("출발역과 도착역이 같을시 예외처리하는 테스트")
	@Test
	void getShortestPath_WhenSameSourceAndTarget_ThrowException() {
		String sourceName = "신도림";
		String targetName = "신도림";

		assertThatThrownBy(() -> pathService.findShortestDistancePath(new PathSearchRequest(sourceName, targetName, "duration")))
				.isInstanceOf(SourceEqualsTargetException.class)
				.hasMessage("출발역과 도착역이 같으면 안돼요.");
	}

	@DisplayName("출발역이나 도착역이 노선에 없을시 예외처리하는 테스트")
	@CsvSource(value = {"우테코,신도림", "신도림,우테코", "우테코,루터회관"})
	@ParameterizedTest
	void getShortestPath_WhenNotExistSourceAndTarget_ThrowException(String sourceName, String targetName) {

		assertThatThrownBy(() -> pathService.findShortestDistancePath(new PathSearchRequest(sourceName, targetName, "duration")))
				.isInstanceOf(NoStationNameExistsException.class)
				.hasMessage("해당역이 존재하지 않아요.");
	}

	@DisplayName("출발역이나 도착역으로 빈 값입력시 예외처리하는 테스트")
	@CsvSource(value = {"'',신도림", "신도림,''", "'',''"})
	@ParameterizedTest
	void getShortestPath_WhenEmptySourceAndTarget_ThrowException(String sourceName, String targetName) {

		assertThatThrownBy(() -> pathService.findShortestDistancePath(new PathSearchRequest(sourceName, targetName, "duration")))
				.isInstanceOf(EmptyStationNameException.class)
				.hasMessage("출발역과 도착역 모두를 입력해주세요.");
	}

	@DisplayName("출발역이나 도착역이 연결되어있지 않을 경우 예외처리하는 테스트")
	@Test
	void getShortestPath_WhenNotConnectedStations_ThrowException() {
		String sourceName = "구로";
		String targetName = "토니";
		String targetNextName = "무늬";
		Station station11 = new Station(11L, targetName);
		Station station12 = new Station(12L, targetNextName);

		when(stationRepository.findByName(sourceName)).thenReturn(Optional.of(station1));
		when(stationRepository.findByName(targetName)).thenReturn(Optional.of(station11));

		Line line3 = new Line(3L, "토니호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-blue-900");
		line3.addLineStation(new LineStation(null, station11.getId(), 40, 10));
		line3.addLineStation(new LineStation(station11.getId(), station12.getId(), 40, 10));

		assertThatThrownBy(() -> pathService.findShortestDistancePath(new PathSearchRequest(sourceName, targetName, "duration")))
				.isInstanceOf(NoPathExistsException.class)
				.hasMessage("해당 경로는 존재하지 않아요.");
	}
}