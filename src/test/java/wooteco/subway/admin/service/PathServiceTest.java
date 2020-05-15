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
	private Station station1;
	private Station station2;
	private Station station3;
	private Station station4;
	private Station station5;


	@BeforeEach
	void setUp() {
		pathService = new PathService(lineStationRepository, stationRepository);

		station1 = new Station(1L, "교대");
		station2 = new Station(2L, "강남");
		station3 = new Station(3L, "양재");
		station4 = new Station(4L, "남부터미널");
		station5 = new Station(5L, "연결X");
		stations = Arrays.asList(station1, station2, station3, station4, station5);

		lineStations = new ArrayList<>();
		lineStations.add(new LineStation(null, station1.getId(), 0, 0));
		lineStations.add(new LineStation(station1.getId(), station2.getId(), 3, 1));
		lineStations.add(new LineStation(null, station1.getId(), 0, 0));
		lineStations.add(new LineStation(station1.getId(), station4.getId(), 2, 1));
		lineStations.add(new LineStation(station4.getId(), station3.getId(), 2, 2));
		lineStations.add(new LineStation(null, station2.getId(), 0, 0));
		lineStations.add(new LineStation(station2.getId(), station3.getId(), 2, 1));
	}

	@Test
	void searchPath_byDistance() {
		when(lineStationRepository.findAll()).thenReturn(lineStations);
		when(stationRepository.findAll()).thenReturn(stations);

		PathInfoResponse pathInfoResponse = pathService
			.searchPath(station1.getId(), station3.getId());

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
			.searchPath(station1.getId(), station3.getId());

		PathResponse shortestDurationPath = pathInfoResponse.getShortestDurationPath();

		assertThat(shortestDurationPath.getStations()).hasSize(3);
		assertThat(shortestDurationPath.getDistance()).isEqualTo(2);
		assertThat(shortestDurationPath.getDuration()).isEqualTo(5);
	}
}