package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineStationCreateRequest;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.LineService;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
	private static final String 강남 = "강남역";
	private static final String 역삼 = "역삼역";
	private static final String 선릉 = "선릉역";
	private static final String 삼성 = "역삼역";

	@Mock
	private LineRepository lineRepository;
	@Mock
	private StationRepository stationRepository;

	private LineService lineService;

	private Line line;
	private Station 강남역;
	private Station 역삼역;
	private Station 선릉역;
	private Station 삼성역;

	@BeforeEach
	void setUp() {
		lineService = new LineService(lineRepository, stationRepository);

		강남역 = new Station(1L, 강남);
		역삼역 = new Station(2L, 역삼);
		선릉역 = new Station(3L, 선릉);
		삼성역 = new Station(4L, 삼성);

		line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
		line.addLineStation(new LineStation(null, 1L, 10, 10));
		line.addLineStation(new LineStation(1L, 2L, 10, 10));
		line.addLineStation(new LineStation(2L, 3L, 10, 10));
	}

	@Test
	void addLineStationAtTheFirstOfLine() {
		when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

		LineStationCreateRequest request = new LineStationCreateRequest(null, 삼성역.getId(), 10,
			10);
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

		LineStationCreateRequest request = new LineStationCreateRequest(강남역.getId(), 삼성역.getId(), 10, 10);
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

		LineStationCreateRequest request = new LineStationCreateRequest(선릉역.getId(),
			삼성역.getId(), 10, 10);
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
		List<Station> stations = Lists.newArrayList(강남역, 역삼역, 삼성역);
		when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
		when(stationRepository.findAllById(anyList())).thenReturn(stations);

		LineDetailResponse lineDetailResponse = lineService.findLineWithStationsById(1L);

		assertThat(lineDetailResponse.getStations()).hasSize(3);
	}
}
