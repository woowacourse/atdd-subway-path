package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
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

	private Line line1;
	private Line line2;
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

		line1 = Line.of("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5).withId(1L);
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 10, 10));
		line1.addLineStation(new LineStation(2L, 3L, 10, 10));

		line2 = Line.of("신분당선", LocalTime.of(5, 30), LocalTime.of(22, 30), 5).withId(2L);
		line2.addLineStation(new LineStation(null, 6L, 10, 10));
		line2.addLineStation(new LineStation(6L, 5L, 10, 10));
		line2.addLineStation(new LineStation(5L, 4L, 10, 10));
		line2.addLineStation(new LineStation(4L, 1L, 10, 10));

	}

	@Test
	void addLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));

		LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10, 10);
		lineService.addLineStation(line1.getId(), request);

		assertThat(line1.getStations()).hasSize(4);

		List<Long> stationIds = line1.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(4L);
		assertThat(stationIds.get(1)).isEqualTo(1L);
		assertThat(stationIds.get(2)).isEqualTo(2L);
		assertThat(stationIds.get(3)).isEqualTo(3L);
	}

	@Test
	void addLineStationBetweenTwo() {
		when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));

		LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(), station4.getId(), 10, 10);
		lineService.addLineStation(line1.getId(), request);

		assertThat(line1.getStations()).hasSize(4);

		List<Long> stationIds = line1.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(4L);
		assertThat(stationIds.get(2)).isEqualTo(2L);
		assertThat(stationIds.get(3)).isEqualTo(3L);
	}

	@Test
	void addLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));

		LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(), station4.getId(), 10, 10);
		lineService.addLineStation(line1.getId(), request);

		assertThat(line1.getStations()).hasSize(4);

		List<Long> stationIds = line1.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
		assertThat(stationIds.get(2)).isEqualTo(3L);
		assertThat(stationIds.get(3)).isEqualTo(4L);
	}

	@Test
	void removeLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));
		lineService.removeLineStation(line1.getId(), 1L);

		assertThat(line1.getStations()).hasSize(2);

		List<Long> stationIds = line1.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(2L);
		assertThat(stationIds.get(1)).isEqualTo(3L);
	}

	@Test
	void removeLineStationBetweenTwo() {
		when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));
		lineService.removeLineStation(line1.getId(), 2L);

		List<Long> stationIds = line1.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(3L);
	}

	@Test
	void removeLineStationAtTheEndOfLine() {
		when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));
		lineService.removeLineStation(line1.getId(), 3L);

		assertThat(line1.getStations()).hasSize(2);

		List<Long> stationIds = line1.getLineStationsId();
		assertThat(stationIds.get(0)).isEqualTo(1L);
		assertThat(stationIds.get(1)).isEqualTo(2L);
	}

	@Test
	void findLineWithStationsById() {
		List<Station> stations = Lists.newArrayList(new Station("강남역"), new Station("역삼역"), new Station("삼성역"));
		when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line1));
		when(stationRepository.findAllById(anyList())).thenReturn(stations);

		LineDetailResponse lineDetailResponse = lineService.findLineWithStationsById(1L);

		assertThat(lineDetailResponse.getStations()).hasSize(3);
	}

	@Test
	void wholeLines() {
		List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"),
			new Station(2L, "역삼역"), new Station(3L, "삼성역"),
			new Station(4L, "양재역"), new Station(5L, "양재시민의숲역"),
			new Station(6L, "청계산입구역"));

		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line1, this.line2));
		when(lineRepository.findById(1L)).thenReturn(Optional.of(this.line1));
		when(lineRepository.findById(2L)).thenReturn(Optional.of(this.line2));
		when(stationRepository.findAllById(Arrays.asList(1L, 2L, 3L)))
			.thenReturn(Arrays.asList(stations.get(0), stations.get(1), stations.get(2)));
		when(stationRepository.findAllById(Arrays.asList(6L, 5L, 4L, 1L)))
			.thenReturn(Arrays.asList(stations.get(5), stations.get(4), stations.get(3), stations.get(0)));

		List<LineDetailResponse> lineDetailResponses = lineService.wholeLines().getLineDetailResponses();

		assertThat(lineDetailResponses).isNotNull();
		assertThat(lineDetailResponses.get(0).getStations().size()).isEqualTo(3);
		assertThat(lineDetailResponses.get(1).getStations().size()).isEqualTo(4);
	}

	@DisplayName("최단 거리 경로와 최소 시간 경로를 찾는다.")
	@Test
	void searchPaths() {
		when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line1, this.line2));
		List<Station> stations = Lists.newArrayList(new Station(6L, "청계산입구역"),
			new Station(5L, "양재시민의숲역"),
			new Station(4L, "양재역"), new Station(1L, "강남역"),
			new Station(2L, "역삼역"), new Station(3L, "삼성역")
		);
		when(stationRepository.findAll()).thenReturn(stations);
		when(stationRepository.findByName("청계산입구역")).thenReturn(Optional.of(new Station(6L, "청계산입구역")));
		when(stationRepository.findByName("삼성역")).thenReturn(Optional.of(new Station(3L, "삼성역")));

		PathResponse pathResponse = lineService.searchPath("청계산입구역", "삼성역", PathType.DISTANCE);

		assertThat(pathResponse.getStations().size()).isEqualTo(6);
		assertThat(pathResponse.getDistance()).isEqualTo(50);
		assertThat(pathResponse.getDuration()).isEqualTo(50);
	}
}
