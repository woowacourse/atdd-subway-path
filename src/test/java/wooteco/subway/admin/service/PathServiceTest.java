package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.InaccessibleStationException;
import wooteco.subway.admin.exception.NonExistentDataException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "삼성역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private PathService pathService;

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineRepository, stationRepository);

		station1 = new Station(1L, STATION_NAME1);
		station2 = new Station(2L, STATION_NAME2);
		station3 = new Station(3L, STATION_NAME3);
		station4 = new Station(4L, STATION_NAME4);

		line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "red");
		line.addLineStation(new LineStation(null, 1L, 10, 10));
		line.addLineStation(new LineStation(1L, 2L, 10, 10));
		line.addLineStation(new LineStation(2L, 3L, 10, 10));
	}

	@Test
	@DisplayName("거리우선 경로 탐색")
	void findPathAsDistanceTest() {
		when(stationRepository.findByName("강남역")).thenReturn(Optional.of(station1));
		when(stationRepository.findByName("선릉역")).thenReturn(Optional.of(station3));
		when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4));
		when(lineRepository.findAll()).thenReturn(Arrays.asList(line));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(station3));

		PathResponse pathResponse = pathService.findPath("강남역", "선릉역", "distance");

		assertThat(pathResponse.getStations()).hasSize(3);
		assertThat(pathResponse.getDistance()).isEqualTo(20);
		assertThat(pathResponse.getDuration()).isEqualTo(20);
	}

	@Test
	@DisplayName("시간우선 경로 탐색")
	void findPathAsDurationTest() {
		when(stationRepository.findByName("강남역")).thenReturn(Optional.of(station1));
		when(stationRepository.findByName("선릉역")).thenReturn(Optional.of(station3));
		when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4));
		when(lineRepository.findAll()).thenReturn(Arrays.asList(line));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(station3));

		PathResponse pathResponse = pathService.findPath("강남역", "선릉역", "duration");

		assertThat(pathResponse.getStations()).hasSize(3);
		assertThat(pathResponse.getDistance()).isEqualTo(20);
		assertThat(pathResponse.getDuration()).isEqualTo(20);
	}

	@Test
	@DisplayName("출발역과 도착역이 같은경우")
	void findSamePathTest() {
		assertThatThrownBy(() -> pathService.findPath("강남역", "강남역", "distance"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("같을");
	}

	@Test
	@DisplayName("길이 연결되어있지 않은 경우")
	void findInaccessiblePathTest() {
		when(stationRepository.findByName("강남역")).thenReturn(Optional.of(station1));
		when(stationRepository.findByName("삼성역")).thenReturn(Optional.of(station4));
		when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4));
		when(lineRepository.findAll()).thenReturn(Arrays.asList(line));

		assertThatThrownBy(() -> pathService.findPath("강남역", "삼성역", "distance"))
			.isInstanceOf(InaccessibleStationException.class)
			.hasMessageContaining("갈 수 없는 역");
	}

	@Test
	@DisplayName("존재하지 않는 역으로 조회하려는 경우")
	void findWithNonExistentStation() {
		assertThatThrownBy(() -> pathService.findPath("강남역", "이상한역", "distance"))
			.isInstanceOf(NonExistentDataException.class)
			.hasMessageContaining("존재 하지 않습니다");
	}
}