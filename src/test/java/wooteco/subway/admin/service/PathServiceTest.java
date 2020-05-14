package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wooteco.subway.admin.domain.PathSearchType.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
	public static final String STATION_NAME1 = "강남역";
	public static final String STATION_NAME2 = "역삼역";
	public static final String STATION_NAME3 = "선릉역";
	public static final String STATION_NAME4 = "삼성역";
	public static final String STATION_NAME5 = "잠실역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private PathService pathService;

	private Line lineOne;
	private Line lineTwo;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;
	private Station station5;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineRepository, stationRepository);

		station1 = new Station(1L, STATION_NAME1);
		station2 = new Station(2L, STATION_NAME2);
		station3 = new Station(3L, STATION_NAME3);
		station4 = new Station(4L, STATION_NAME4);
		station5 = new Station(5L, STATION_NAME5);

		lineOne = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
		lineTwo = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

		lineOne.addLineStation(new LineStation(null, 1L, 0, 0));
		lineOne.addLineStation(new LineStation(1L, 2L, 5, 3));
		lineOne.addLineStation(new LineStation(2L, 3L, 5, 2));
		lineOne.addLineStation(new LineStation(3L, 4L, 3, 4));

		lineTwo.addLineStation(new LineStation(null, 4L, 0, 0));
		lineTwo.addLineStation(new LineStation(4L, 2L, 9, 1));
		lineTwo.addLineStation(new LineStation(2L, 5L, 3, 5));
	}

	@DisplayName("최단거리 경로를 잘 찾아내는지 확인")
	@Test
	void shortestDistancePath() {
		when(lineRepository.findAll()).thenReturn(Arrays.asList(lineOne, lineTwo));
		when(stationRepository.findByName(STATION_NAME1)).thenReturn(Optional.of(station1));
		when(stationRepository.findByName(STATION_NAME4)).thenReturn(Optional.of(station4));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(station3));
		when(stationRepository.findById(4L)).thenReturn(Optional.of(station4));

		ShortestPathResponse expected = new ShortestPathResponse(
			StationResponse.listOf(Arrays.asList(station1, station2, station3, station4)), 13, 9
		);
		assertThat(pathService.getShortestPath(STATION_NAME1, STATION_NAME4, DISTANCE)).isEqualTo(expected);
	}

	@DisplayName("최단시간 경로를 잘 찾아내는지 확인")
	@Test
	void shortestDurationPath() {
		when(lineRepository.findAll()).thenReturn(Arrays.asList(lineOne, lineTwo));
		when(stationRepository.findByName(STATION_NAME1)).thenReturn(Optional.of(station1));
		when(stationRepository.findByName(STATION_NAME4)).thenReturn(Optional.of(station4));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(4L)).thenReturn(Optional.of(station4));

		ShortestPathResponse expected = new ShortestPathResponse(
			StationResponse.listOf(Arrays.asList(station1, station2, station4)), 14, 4
		);
		assertThat(pathService.getShortestPath(STATION_NAME1, STATION_NAME4, DURATION)).isEqualTo(expected);
	}
}
