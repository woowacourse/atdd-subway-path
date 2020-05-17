package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

/**
 *    class description
 *
 *    @author HyungJu An, KuenHwi Choi
 */
@ExtendWith(MockitoExtension.class)
class GraphServiceTest {
	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private GraphService graphService;

	private Line line1;
	private Line line2;
	private List<Station> stations;

	@BeforeEach
	void setUp() {
		graphService = new GraphService(lineRepository, stationRepository);

		line1 = Line.of("2호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5).withId(1L);
		line1.addLineStation(LineStation.of(null, 1L, 10, 10));
		line1.addLineStation(LineStation.of(1L, 2L, 10, 10));
		line1.addLineStation(LineStation.of(2L, 3L, 10, 10));

		line2 = Line.of("신분당선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5).withId(2L);
		line2.addLineStation(LineStation.of(null, 6L, 10, 10));
		line2.addLineStation(LineStation.of(6L, 5L, 10, 10));
		line2.addLineStation(LineStation.of(5L, 4L, 10, 10));
		line2.addLineStation(LineStation.of(4L, 1L, 10, 10));

		stations = Lists.newArrayList(Station.of("청계산입구역").withId(6L),
			Station.of("양재시민의숲역").withId(5L),
			Station.of("양재역").withId(4L), Station.of("강남역").withId(1L),
			Station.of("역삼역").withId(2L), Station.of("삼성역").withId(3L)
		);
	}

	@DisplayName("최단 거리 경로와 최소 시간 경로를 찾는다.")
	@Test
	void searchPaths() {
		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line1, this.line2));
		when(stationRepository.findAll()).thenReturn(stations);
		when(stationRepository.findByName("청계산입구역")).thenReturn(Optional.of(Station.of("청계산입구역").withId(6L)));
		when(stationRepository.findByName("삼성역")).thenReturn(Optional.of(Station.of("삼성역").withId(3L)));

		PathResponse pathResponse = graphService.searchPath("청계산입구역", "삼성역", PathType.DISTANCE);

		assertThat(pathResponse.getStations().size()).isEqualTo(6);
		assertThat(pathResponse.getDistance()).isEqualTo(50);
		assertThat(pathResponse.getDuration()).isEqualTo(50);
	}

	@DisplayName("출발지와 도착지가 같을 경우 예외처리한다.")
	@Test
	void searchPaths2() {
		assertThatThrownBy(() -> graphService.searchPath("강남역", "강남역", PathType.DISTANCE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("동일");
	}

	@DisplayName("출발지와 도착지 연결되어 있지 않을 경우 예외처리한다.")
	@Test
	void searchPaths3() {
		Line line3 = Line.of("8호선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5).withId(3L);
		line3.addLineStation(LineStation.of(null, 11L, 0, 0));
		stations.add(Station.of("암사역").withId(11L));

		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line1, this.line2, line3));
		when(stationRepository.findAll()).thenReturn(stations);
		when(stationRepository.findByName("청계산입구역")).thenReturn(Optional.of(Station.of("청계산입구역").withId(6L)));
		when(stationRepository.findByName("암사역")).thenReturn(Optional.of(Station.of("암사역").withId(11L)));

		assertThatThrownBy(() -> graphService.searchPath("청계산입구역", "암사역", PathType.DISTANCE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("연결");
	}

	@DisplayName("존재하지 않는 출발역을 조회할 경우 예외처리한다.")
	@Test
	void searchPaths4() {
		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line1, this.line2));
		when(stationRepository.findAll()).thenReturn(stations);

		assertThatThrownBy(() -> graphService.searchPath("홍대입구역", "삼성역", PathType.DISTANCE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("출발역");
	}

	@DisplayName("존재하지 않는 도착역을 조회할 경우 예외처리한다.")
	@Test
	void searchPaths5() {
		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line1, this.line2));
		when(stationRepository.findAll()).thenReturn(stations);
		when(stationRepository.findByName("청계산입구역")).thenReturn(Optional.of(Station.of("청계산입구역").withId(6L)));

		assertThatThrownBy(() -> graphService.searchPath("청계산입구역", "홍대입구역", PathType.DISTANCE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("도착역");
	}
}