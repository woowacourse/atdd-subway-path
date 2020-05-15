package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
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
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.exception.InaccessibleStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
	private static final String STATION_NAME1 = "강남역";
	private static final String STATION_NAME2 = "역삼역";
	private static final String STATION_NAME3 = "선릉역";
	private static final String STATION_NAME4 = "삼성역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private LineService lineService;

	private Line line;
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;

	@BeforeEach
	void setUp() {
		lineService = new LineService(lineRepository, stationRepository);

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
	void addLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10, 10);
		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Long> stationIds = line.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(4L);
		assertThat(stationIds.get(1)).isEqualTo(1L);
		assertThat(stationIds.get(2)).isEqualTo(2L);
		assertThat(stationIds.get(3)).isEqualTo(3L);
	}

	@Test
	void addLineStationBetweenTwo() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(), station4.getId(), 10, 10);
		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Long> stationIds = line.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(4L);
		assertThat(stationIds.get(2)).isEqualTo(2L);
		assertThat(stationIds.get(3)).isEqualTo(3L);
	}

	@Test
	void addLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(), station4.getId(), 10, 10);
		lineService.addLineStation(line.getId(), request);

		assertThat(line.getStations()).hasSize(4);

		List<Long> stationIds = line.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(3L);
		assertThat(stationIds.get(3)).isEqualTo(4L);
	}

	@Test
	void removeLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 1L);

		assertThat(line.getStations()).hasSize(2);

		List<Long> stationIds = line.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(2L);
		assertThat(stationIds.get(1)).isEqualTo(3L);
	}

	@Test
	void removeLineStationBetweenTwo() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 2L);

		List<Long> stationIds = line.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(3L);
	}

	@Test
	void removeLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
		lineService.removeLineStation(line.getId(), 3L);

		assertThat(line.getStations()).hasSize(2);

		List<Long> stationIds = line.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
	}

	@Test
	void findLineWithStationsById() {
		when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(station3));

		LineDetailResponse lineDetailResponse = lineService.findLineWithStationsById(1L);

		assertThat(lineDetailResponse.getStations()).hasSize(3);
	}

	@Test
	@DisplayName("전체 노선과 노선의 역들을 가져오는 기능")
	void wholeLinesTest() {
		Line line2 = new Line(2L, "25호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "red");
		line2.addLineStation(new LineStation(null, 4L, 10, 10));

		when(lineRepository.findAll()).thenReturn(Arrays.asList(line, line2));
		when(lineRepository.findById(1L)).thenReturn(Optional.of(line));
		when(lineRepository.findById(2L)).thenReturn(Optional.of(line2));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(station3));
		when(stationRepository.findById(4L)).thenReturn(Optional.of(station4));
		WholeSubwayResponse response = lineService.wholeLines();

		LineDetailResponse firstLineDetailResponse = response.getLineDetailResponses().get(0);
		LineDetailResponse secondLineDetailResponse = response.getLineDetailResponses().get(1);
		assertThat(firstLineDetailResponse.getStations()).hasSize(3);
		assertThat(secondLineDetailResponse.getStations()).hasSize(1);
	}

	@Test
	void findPathTest() {
		when(stationRepository.findByName("강남역")).thenReturn(Optional.of(station1));
		when(stationRepository.findByName("선릉역")).thenReturn(Optional.of(station3));
		when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4));
		when(lineRepository.findAll()).thenReturn(Arrays.asList(line));
		when(stationRepository.findById(1L)).thenReturn(Optional.of(station1));
		when(stationRepository.findById(2L)).thenReturn(Optional.of(station2));
		when(stationRepository.findById(3L)).thenReturn(Optional.of(station3));

		List<PathResponse> pathResponses = lineService.findPath("강남역", "선릉역");
		PathResponse pathResponse = pathResponses.get(0);

		assertThat(pathResponse.getStations()).hasSize(3);
		assertThat(pathResponse.getDistance()).isEqualTo(20);
		assertThat(pathResponse.getDuration()).isEqualTo(20);
	}

	@Test
	@DisplayName("출발역과 도착역이 같은경우")
	void findSamePathTest() {
		assertThatThrownBy(() -> lineService.findPath("강남역", "강남역"))
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

		assertThatThrownBy(() -> lineService.findPath("강남역", "삼성역"))
			.isInstanceOf(InaccessibleStationException.class)
			.hasMessageContaining("갈 수 없는 역");
	}
}
