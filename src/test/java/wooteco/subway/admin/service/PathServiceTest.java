package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.SearchType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationNamesException;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private PathService pathService;
	private List<Station> stations;
	private List<LineStation> lineStations;

	@BeforeEach
	void setUp() {
		pathService = new PathService(lineRepository, stationRepository);
		stations = Arrays.asList(new Station(1L, "강남역"),
				new Station(2L, "역삼역"),
				new Station(3L, "선릉역"),
				new Station(4L, "잠실역"));
		lineStations = Arrays.asList(new LineStation(null, 1L, 10, 5),
				new LineStation(1L, 2L, 10, 5),
				new LineStation(2L, 3L, 10, 5),
				new LineStation(3L, 4L, 10, 5));
	}

	@DisplayName("출발역이 존재 하지 않은 역인 경우 예외 발생")
	@Test
	void existSourceStations() {
		when(stationRepository.notExistsByName(eq("사당역"))).thenReturn(true);
		when(stationRepository.notExistsByName(eq("역삼역"))).thenReturn(false);

		assertThatThrownBy(() -> {
			pathService.searchPath("사당역", "역삼역", SearchType.DISTANCE);
		}).isInstanceOf(NotExistStationException.class);
	}

	@DisplayName("도착역이 존재 하지 않은 역인 경우 예외 발생")
	@Test
	void existTargetStations() {
		when(stationRepository.notExistsByName(eq("강남역"))).thenReturn(false);
		when(stationRepository.notExistsByName(eq("의정부역"))).thenReturn(true);

		assertThatThrownBy(() -> {
			pathService.searchPath("강남역", "의정부역", SearchType.DISTANCE);
		}).isInstanceOf(NotExistStationException.class);
	}

	@DisplayName("출발역과 도착역이 같은 경우 예외 발생")
	@Test
	void duplicatedStations() {
		assertThatThrownBy(() -> {
			pathService.searchPath("source", "source", SearchType.DISTANCE);
		}).isInstanceOf(DuplicatedStationNamesException.class);
	}

	@DisplayName("최단 거리를 기준으로 경로 조회")
	@Test
	void searchPathByShortestDistance() {
		setUpMock();

		PathResponse pathResponse = pathService.searchPath("강남역", "잠실역", SearchType.DISTANCE);

		assertThat(pathResponse.getStations().size()).isEqualTo(4);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(30);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(15);
	}

	@DisplayName("최소 시간을 기준으로 경로 조회")
	@Test
	void searchPathByShortestDuration() {
		setUpMock();

		PathResponse pathResponse = pathService.searchPath("강남역", "잠실역", SearchType.DURATION);

		assertThat(pathResponse.getStations().size()).isEqualTo(4);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(30);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(15);
	}

	private void setUpMock() {
		when(stationRepository.notExistsByName(any())).thenReturn(false);
		when(stationRepository.findAll()).thenReturn(stations);
		when(lineRepository.findAllLineStations()).thenReturn(lineStations);
		when(stationRepository.findByName("강남역")).thenReturn(Optional.of(new Station(1L, "강남역")));
		when(stationRepository.findByName("잠실역")).thenReturn(Optional.of(new Station(4L, "잠실역")));
		when(lineRepository.findLineStationByPreStationIdAndStationId(1L, 2L))
				.thenReturn(lineStations.get(1));
		when(lineRepository.findLineStationByPreStationIdAndStationId(2L, 3L))
				.thenReturn(lineStations.get(2));
		when(lineRepository.findLineStationByPreStationIdAndStationId(3L, 4L))
				.thenReturn(lineStations.get(3));
	}
}
