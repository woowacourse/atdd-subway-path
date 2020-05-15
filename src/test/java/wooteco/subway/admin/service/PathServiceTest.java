package wooteco.subway.admin.service;

import org.assertj.core.util.Lists;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "잠실역";
    private static final String STATION_NAME6 = "석촌역";
    private static final String STATION_NAME7 = "가락역";

    private PathService pathService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        Station station1 = new Station(1L, STATION_NAME1);
        Station station2 = new Station(2L, STATION_NAME2);
        Station station3 = new Station(3L, STATION_NAME3);
        Station station4 = new Station(4L, STATION_NAME4);
        Station station5 = new Station(5L, STATION_NAME5);
        Station station6 = new Station(6L, STATION_NAME6);
        Station station7 = new Station(7L, STATION_NAME7);

        Line line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line line2 = new Line(2L, "8호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        line1.addLineStation(new LineStation(null, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 3));
        line1.addLineStation(new LineStation(2L, 3L, 10, 3));
        line1.addLineStation(new LineStation(3L, 4L, 10, 3));
        line1.addLineStation(new LineStation(4L, 5L, 10, 3));
        line1.addLineStation(new LineStation(5L, 6L, 10, 3));

        line2.addLineStation(new LineStation(null, 2L, 11, 10));
        line2.addLineStation(new LineStation(2L, 4L, 11, 10));
        line2.addLineStation(new LineStation(4L, 6L, 11, 10));

        List<Line> lines = Lists.newArrayList(line1, line2);
        List<Station> stations = Lists.newArrayList(station1, station2, station3, station4, station5, station6);

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAll()).thenReturn(stations);
    }

    @Test
    void searchPath_byDistance() {
        PathSearchResponse pathSearchResponse = pathService.searchPath(STATION_NAME1, STATION_NAME6, PathType.DISTANCE);

        assertThat(pathSearchResponse.getDistance()).isEqualTo(32);
        assertThat(pathSearchResponse.getStations().size()).isEqualTo(4);
    }

    @Test
    void searchPath_byDuration() {
        PathSearchResponse pathSearchResponse2 = pathService.searchPath(STATION_NAME1, STATION_NAME6, PathType.DURATION);

        assertThat(pathSearchResponse2.getDuration()).isEqualTo(15);
        assertThat(pathSearchResponse2.getStations().size()).isEqualTo(6);
    }

    @Test
    void searchPath_unReachable() {
        assertThatThrownBy(() -> pathService.searchPath(STATION_NAME1, STATION_NAME7, PathType.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void searchPath_sameSourceTarget() {
        assertThatThrownBy(() -> pathService.searchPath(STATION_NAME1, STATION_NAME1, PathType.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void searchPath_inValidStation() {
        String notExistStationName = "천호역";

        assertThatThrownBy(() -> pathService.searchPath(notExistStationName, STATION_NAME7, PathType.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
