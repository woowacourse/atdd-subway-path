package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import wooteco.subway.admin.domain.DijkstraEdgeWeightType;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.exceptions.UnconnectedStationsException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
		                         new Station(4L, "잠실역"),
		                         new Station(5L, "서울역"));
		lineStations = Arrays.asList(new LineStation(null, 1L, 10, 5),
		                             new LineStation(1L, 2L, 10, 5),
		                             new LineStation(2L, 3L, 10, 5),
		                             new LineStation(3L, 4L, 10, 5),
		                             new LineStation(null, 5L, 10, 5));
	}

	@DisplayName("출발역과 도착역이 존재하는 역인지 확인")
	@Test
	void existStations() {
		assertThatThrownBy(() -> {
			pathService.searchPath(6L, 7L, DijkstraEdgeWeightType.DISTANCE);
		}).isInstanceOf(NotExistStationException.class);
	}

	@DisplayName("최단 거리를 기준으로 경로 조회")
	@Test
	void searchPathByShortestDistance() {
		setUpMock();

		PathResponse pathResponse = pathService.searchPath(1L, 4L, DijkstraEdgeWeightType.DISTANCE);

		assertThat(pathResponse.getStationNames().size()).isEqualTo(4);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(30);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(15);
	}

	@DisplayName("최소 시간을 기준으로 경로 조회")
	@Test
	void searchPathByShortestDuration() {
		setUpMock();

		PathResponse pathResponse = pathService.searchPath(1L, 4L, DijkstraEdgeWeightType.DURATION);

		assertThat(pathResponse.getStationNames().size()).isEqualTo(4);
		assertThat(pathResponse.getTotalDistance()).isEqualTo(30);
		assertThat(pathResponse.getTotalDuration()).isEqualTo(15);
	}

	@DisplayName("경로가 없는 출발역과 도착역으로 경로 탐색 시 UnconnectedStationsException이 발생하는지 확인")
	@Test
	void ifUnconnectedStationsInputThenThrowException() {
		setUpMock();

		assertThatThrownBy(() -> pathService.searchPath(1L, 5L, DijkstraEdgeWeightType.DISTANCE))
				.isInstanceOf(UnconnectedStationsException.class);
	}

	private void setUpMock() {
		when(stationRepository.findAll()).thenReturn(stations);
		when(lineRepository.findAllLineStations()).thenReturn(lineStations);
		when(stationRepository.findById(1L)).thenReturn(Optional.of(new Station(1L, "강남역")));
		when(stationRepository.findById(4L)).thenReturn(Optional.of(new Station(4L, "잠실역")));
		when(stationRepository.findById(5L)).thenReturn(Optional.of(new Station(5L, "서울역")));
	}
}
