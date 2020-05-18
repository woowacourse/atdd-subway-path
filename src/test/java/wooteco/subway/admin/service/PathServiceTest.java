package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.PathSearchRequest;
import wooteco.subway.admin.dto.response.ShortestPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

		ShortestPathResponse shortestPath = pathService.findShortestDistancePath(new PathSearchRequest(sourceName, targetName, criteria));

		for (int i = 0; i < shortestPath.getPath().size(); i++) {
			assertEquals(shortestPath.getPath().get(i), expectedPath.get(i));
		}

		assertEquals(shortestPath.getDistance(), expectedDistance);
		assertEquals(shortestPath.getDuration(), expectedDuration);
	}
}