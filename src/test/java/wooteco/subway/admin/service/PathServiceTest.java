package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathInfoResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineStationRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

	@Mock
	LineStationRepository lineStationRepository;

	@Mock
	StationRepository stationRepository;

	private PathService pathService;
	private List<Station> stations;
	private List<LineStation> lineStations;
	private Station 교대;
	private Station 강남;
	private Station 양재;
	private Station 남부터미널;
	private Station 폐쇄된역;


	@BeforeEach
	void setUp() {
		pathService = new PathService(lineStationRepository, stationRepository);

		교대 = new Station(1L, "교대");
		강남 = new Station(2L, "강남");
		양재 = new Station(3L, "양재");
		남부터미널 = new Station(4L, "남부터미널");
		폐쇄된역 = new Station(5L, "폐쇄된역");
		stations = Arrays.asList(교대, 강남, 양재, 남부터미널, 폐쇄된역);

		lineStations = new ArrayList<>();
		lineStations.add(new LineStation(null, 교대.getId(), 0, 0));
		lineStations.add(new LineStation(교대.getId(), 강남.getId(), 3, 1));
		lineStations.add(new LineStation(null, 교대.getId(), 0, 0));
		lineStations.add(new LineStation(교대.getId(), 남부터미널.getId(), 2, 1));
		lineStations.add(new LineStation(남부터미널.getId(), 양재.getId(), 2, 2));
		lineStations.add(new LineStation(null, 강남.getId(), 0, 0));
		lineStations.add(new LineStation(강남.getId(), 양재.getId(), 2, 1));
	}

	@Test
	void searchPath_byDistance() {
		when(lineStationRepository.findAll()).thenReturn(lineStations);
		when(stationRepository.findAll()).thenReturn(stations);

		PathInfoResponse pathInfoResponse = pathService
			.searchPath(교대.getId(), 양재.getId());

		PathResponse shortestDistancePath = pathInfoResponse.getShortestDistancePath();

		assertThat(shortestDistancePath.getStations()).hasSize(3);
		assertThat(shortestDistancePath.getDistance()).isEqualTo(4);
		assertThat(shortestDistancePath.getDuration()).isEqualTo(3);
	}

	@Test
	void searchPath_byDuration() {
		when(lineStationRepository.findAll()).thenReturn(lineStations);
		when(stationRepository.findAll()).thenReturn(stations);

		PathInfoResponse pathInfoResponse = pathService
			.searchPath(교대.getId(), 양재.getId());

		PathResponse shortestDurationPath = pathInfoResponse.getShortestDurationPath();

		assertThat(shortestDurationPath.getStations()).hasSize(3);
		assertThat(shortestDurationPath.getDistance()).isEqualTo(2);
		assertThat(shortestDurationPath.getDuration()).isEqualTo(5);
	}
}