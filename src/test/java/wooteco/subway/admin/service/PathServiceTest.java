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
import wooteco.subway.admin.repository.LineStationRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

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

    private PathService pathService;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineStationRepository lineStationRepository;

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

        when(stationRepository.findByName(STATION_NAME1)).thenReturn(Optional.of(station1));
        when(stationRepository.findByName(STATION_NAME4)).thenReturn(Optional.of(station4));
        when(stationRepository.findByName(STATION_NAME7)).thenReturn(Optional.of(station7));
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(station1));
        when(stationRepository.findById(2L)).thenReturn(Optional.ofNullable(station2));
        when(stationRepository.findById(3L)).thenReturn(Optional.ofNullable(station3));
        when(stationRepository.findById(4L)).thenReturn(Optional.ofNullable(station4));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(station5));
        when(stationRepository.findById(6L)).thenReturn(Optional.ofNullable(station6));
        when(stationRepository.findById(7L)).thenReturn(Optional.ofNullable(station7));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(lineStationRepository.findById(null, 1L)).thenReturn(Optional.ofNullable(lineStation1));
        when(lineStationRepository.findById(1L, 2L)).thenReturn(Optional.ofNullable(lineStation2));
        when(lineStationRepository.findById(2L, 3L)).thenReturn(Optional.ofNullable(lineStation3));
        when(lineStationRepository.findById(3L, 4L)).thenReturn(Optional.ofNullable(lineStation4));
        when(lineStationRepository.findById(2L, 5L)).thenReturn(Optional.ofNullable(lineStation5));
        when(lineStationRepository.findById(5L, 4L)).thenReturn(Optional.ofNullable(lineStation6));
        when(lineStationRepository.findById(6L, 7L)).thenReturn(Optional.ofNullable(lineStation7));

        pathService = new PathService(lineRepository, stationRepository, lineStationRepository);
    }

    @DisplayName("최소 거리 조회 테스트")
    @Test
    public void shortestDistance() {
        SearchPathResponse searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, "distance");
        assertThat(searchPathResponse.getPathStationNames()).contains("가깝고느린역");
    }

    @DisplayName("최소 시간 조회 테스트")
    @Test
    public void shortestDuration() {
        SearchPathResponse searchPathResponse = pathService.searchPath(STATION_NAME1, STATION_NAME4, "duration");
        assertThat(searchPathResponse.getPathStationNames()).contains("선릉역");
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    public void sameStartTarget() {
        assertThatThrownBy(() -> pathService.searchPath(STATION_NAME1, STATION_NAME1, "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시작역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결되지 않은 경우")
    @Test
    public void notConnected() {
        assertThatThrownBy(() -> pathService.searchPath(STATION_NAME1, STATION_NAME7, "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("두 역이 연결되어있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    public void notExistStation() {
        assertThatThrownBy(() -> pathService.searchPath("X", STATION_NAME7, "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역이 존재하지 않습니다.");
    }

}
