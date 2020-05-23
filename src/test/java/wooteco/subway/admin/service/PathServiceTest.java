package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static wooteco.subway.admin.domain.EdgeType.DISTANCE;
import static wooteco.subway.admin.domain.EdgeType.DURATION;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "가깝고느린역";
    private static final String STATION_NAME6 = "연결되지 않은 역";
    private static final String STATION_NAME7 = "연결되지 않은 역2";
    private static final String NOT_EXIST_STATION = "X";

    private PathService pathService;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private Line line1;
    private Line line2;
    private Line line3;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Station station7;
    private LineStation lineStation1;
    private LineStation lineStation2;
    private LineStation lineStation3;
    private LineStation lineStation4;
    private LineStation lineStation5;
    private LineStation lineStation6;
    private LineStation lineStation7;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        station6 = new Station(6L, STATION_NAME6);
        station7 = new Station(7L, STATION_NAME7);

        line1 = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2 = new Line("11호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3 = new Line("21호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        lineStation1 = new LineStation(null, 1L, 10, 10);
        lineStation2 = new LineStation(1L, 2L, 10, 10);
        lineStation3 = new LineStation(2L, 3L, 10, 10);
        lineStation4 = new LineStation(3L, 4L, 10, 10);
        lineStation5 = new LineStation(2L, 5L, 1, 100);
        lineStation6 = new LineStation(5L, 4L, 1, 100);
        lineStation7 = new LineStation(6L, 7L, 1, 1);

        line1.addLineStation(lineStation1);
        line1.addLineStation(lineStation2);
        line1.addLineStation(lineStation3);
        line1.addLineStation(lineStation4);
        line2.addLineStation(lineStation5);
        line2.addLineStation(lineStation6);
        line3.addLineStation(lineStation7);

        lineRepository.save(line1);
        lineRepository.save(line2);
        lineRepository.save(line3);

        pathService = new PathService(lineRepository, stationRepository);
    }

    @DisplayName("최소 거리 조회 테스트")
    @Test
    public void shortestDistance() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6, station7));

        SearchPathResponse searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, DISTANCE);
        assertThat(searchPathResponse.getPathStationNames()).isEqualTo(Arrays.asList("강남역", "역삼역", "가깝고느린역", "삼성역"));
    }

    @DisplayName("최소 시간 조회 테스트")
    @Test
    public void shortestDuration() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6, station7));

        SearchPathResponse searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, DURATION);
        assertThat(searchPathResponse.getPathStationNames()).isEqualTo(Arrays.asList("강남역", "역삼역", "선릉역", "삼성역"));
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    public void sameStartTarget() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1));

        assertThatThrownBy(() -> pathService.searchPath(STATION_NAME1, STATION_NAME1, DURATION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시작역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우")
    @Test
    public void notConnected() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6, station7));

        assertThatThrownBy(() -> pathService.searchPath(STATION_NAME1, STATION_NAME7, DURATION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("두 역이 연결되어있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    public void notExistStation() {
        when(stationRepository.findByName(STATION_NAME7)).thenReturn(Optional.of(station7));

        assertThatThrownBy(() -> pathService.searchPath(NOT_EXIST_STATION, STATION_NAME7, DURATION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역이 존재하지 않습니다.");
    }
}
