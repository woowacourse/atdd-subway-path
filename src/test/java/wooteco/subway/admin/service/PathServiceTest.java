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
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
	private List<Long> stationIds;

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
		stationIds = Arrays.asList(1L, 2L, 3L, 4L);
	}

	@DisplayName("출발역과 도착역이 존재하는 역인지 확인")
	@Test
	void existStations() {
		when(stationRepository.existsByName(any())).thenReturn(false);

		assertThatThrownBy(() -> {
			pathService.searchPath("source", "target", SearchType.DISTANCE);
		}).isInstanceOf(NotExistStationException.class);
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
		when(stationRepository.existsByName(any())).thenReturn(true);
		when(stationRepository.findAllIds()).thenReturn(Arrays.asList(1L, 2L, 3L, 4L));
		when(lineRepository.findAllLineStations()).thenReturn(lineStations);
		when(stationRepository.findByName("강남역")).thenReturn(Optional.of(new Station(1L, "강남역")));
		when(stationRepository.findByName("잠실역")).thenReturn(Optional.of(new Station(4L, "잠실역")));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(new Station(1L, "강남역")));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(new Station(2L, "역삼역")));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(new Station(3L, "선릉역")));
		when(stationRepository.findById(4L)).thenReturn(Optional.of(new Station(4L, "잠실역")));
		when(lineRepository.findLineStationByPreStationIdAndStationId(1L, 2L))
				.thenReturn(lineStations.get(1));
		when(lineRepository.findLineStationByPreStationIdAndStationId(2L, 3L))
				.thenReturn(lineStations.get(2));
		when(lineRepository.findLineStationByPreStationIdAndStationId(3L, 4L))
				.thenReturn(lineStations.get(3));
	}
}
