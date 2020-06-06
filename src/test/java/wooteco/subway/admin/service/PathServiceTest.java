package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.graph.PathNotFoundException;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "삼성역";
	private static final String DURATION = "DURATION";

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;
	private PathService pathService;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineRepository, stationRepository);

		station1 = new Station(1L, STATION_NAME1);
		station2 = new Station(2L, STATION_NAME2);
		station3 = new Station(3L, STATION_NAME3);
		station4 = new Station(4L, STATION_NAME4);

		line = new Line(1L, "2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5);
		line.addLineStation(new LineStation(null, 1L, 10, 10));
		line.addLineStation(new LineStation(1L, 2L, 10, 10));
		line.addLineStation(new LineStation(2L, 3L, 10, 10));
	}

	@DisplayName("출발역과 도착역이 같은 경우")
	@Test
	void departStationIsArrivalStation() {
		assertThatThrownBy(() -> pathService.findPath(1L, 1L, WeightType.DURATION))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("출발역과 도착역이 같습니다.");
	}

	@DisplayName("출발역과 도착역 사이의 길이 없는 경우")
	@Test
	void noPath() {
		Line newLine = new Line(2L, "8호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 8);
		String 석촌역 = "석촌역";
		Station 연결되지않은_역 = new Station(5L, 석촌역);

		saveMockData(newLine, 연결되지않은_역);

		newLine.addLineStation(new LineStation(null, 연결되지않은_역.getId(), 0, 0));

		assertThatThrownBy(() -> pathService.findPath(1L, 연결되지않은_역.getId(), WeightType.DURATION))
			.isInstanceOf(PathNotFoundException.class)
			.hasMessage(PathNotFoundException.STATION_NOT_FOUND_MESSAGE);
	}

	@DisplayName("출발역이나 도착역이 존재하지 않는 역인 경우")
	@Test
	void stationNotExist() {
		Line newLine = new Line(2L, "8호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 8);
		String 석촌역 = "석촌역";
		Station 연결되지않은_역 = new Station(5L, 석촌역);

		saveMockData(newLine, 연결되지않은_역);

		String 히히역 = "히히역";
		assertThatThrownBy(() -> {
			pathService.findPath(station1.getId(), 10L, WeightType.DURATION);
		})
			.isInstanceOf(PathNotFoundException.class)
			.hasMessage("유효하지 않은 역을 입력했습니다.");
	}

	private void saveMockData(Line newLine, Station station) {
		List<Line> mockLines = Arrays.asList(line, newLine);
		when(lineRepository.findAll()).thenReturn(mockLines);

		List<Station> mockStations = Arrays.asList(station, station1, station2, station3, station4);
		when(stationRepository.findAll()).thenReturn(mockStations);
	}
}
