package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PathService.class, GraphService.class})
class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

    @MockBean
    private LineRepository lineRepository;
    @MockBean
    private StationRepository stationRepository;

    private GraphService graphService = new GraphService();

    private PathService pathService;

    private Line line;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository, graphService);
        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
        line.addLineStation(new LineStation(3L, 4L, 10, 10));
    }

    @Test
    void findPathTest() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line));
        when(stationRepository.findAllById(anyList())).thenReturn(
            Arrays.asList(station1, station2, station3, station4));
        when(stationRepository.findByName(station1.getName())).thenReturn(Optional.of(station1));
        when(stationRepository.findByName(station3.getName())).thenReturn(Optional.of(station3));

        PathResponse pathResponse = pathService.showPaths(station1.getName(), station3.getName());
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        final List<StationResponse> stations = pathResponse.getStations();
        final List<Long> ids = stations.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());
        assertThat(stations.size()).isEqualTo(3);
        assertThat(ids).containsExactly(station1.getId(), station2.getId(), station3.getId());
    }
}