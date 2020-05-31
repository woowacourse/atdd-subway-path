package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.NoSuchElementException;

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
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.NotConnectEdgeException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME1 = "양재시민역";
    private static final String STATION_NAME2 = "양재역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "역삼역";
    private static final String STATION_NAME5 = "강남역";
    private static final String STATION_NAME6 = "중계역";
    private static final String STATION_NAME7 = "하계역";

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    private Line line1;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);

        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-red-300");
        line2 = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-yellow-300");

        line1.addLineStation(new LineStation(null, 5L, 0, 0));
        line1.addLineStation(new LineStation(5L, 4L, 15, 15));
        line1.addLineStation(new LineStation(4L, 3L, 5, 5));

        line2.addLineStation(new LineStation(null, 5L, 0, 0));
        line2.addLineStation(new LineStation(5L, 2L, 15, 15));
        line2.addLineStation(new LineStation(2L, 1L, 5, 5));
    }

    @DisplayName("최단 거리 경로를 조회하는 메서드 테스트")
    @Test
    void findShortestPathByDistanceTest() {
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        PathRequest pathRequest = new PathRequest(station1.getId(), station4.getId(), PathType.DISTANCE);

        PathResponse pathResponse = pathService.findShortestPathByDistance(pathRequest);

        assertThat(pathResponse.getStations().size()).isEqualTo(4);
        assertThat(pathResponse.getDistance()).isEqualTo(35);
        assertThat(pathResponse.getDuration()).isEqualTo(35);
    }

    @DisplayName("출발역과 도착역이 연결되어있지 않은 경우 테스트")
    @Test
    void notConnectStationTest() {
        Station station6 = new Station(6L, STATION_NAME6);
        Station station7 = new Station(7L, STATION_NAME7);

        Line line3 = new Line(3L, "7호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-blue-300");

        line3.addLineStation(new LineStation(null, 6L, 0, 0));
        line3.addLineStation(new LineStation(6L, 7L, 15, 15));

        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6, station7));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));

        PathRequest pathRequest = new PathRequest(station1.getId(), station7.getId(), PathType.DISTANCE);

        assertThatThrownBy(() -> pathService.findShortestPathByDistance(pathRequest))
            .isInstanceOf(NotConnectEdgeException.class)
            .hasMessage("출발 지점으로부터 도착 지점까지 갈 수 없습니다!");

    }

    @DisplayName("존재하지 않은 역이 들어왔을 경우 예외 처리")
    @Test
    void notExistStationTest(){
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        PathRequest pathRequest = new PathRequest(6L, station4.getId(), PathType.DISTANCE);

        assertThatThrownBy(() -> pathService.findShortestPathByDistance(pathRequest))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("등록되어있지 않은 역입니다.");
    }
}
