package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.exception.WrongPathException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "양재역";
    private static final String STATION_NAME5 = "양재시민의숲역";
    private static final String STATION_NAME6 = "신촌역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    private Line line1;
    private Line line2;
    private Line line3;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(5L, STATION_NAME5);
        station6 = new Station(6L, STATION_NAME6);

        line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addEdge(new Edge(null, 1L, 10, 10));
        line1.addEdge(new Edge(1L, 2L, 5, 15));
        line1.addEdge(new Edge(2L, 3L, 10, 10));

        line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addEdge(new Edge(null, 1L, 10, 10));
        line2.addEdge(new Edge(1L, 4L, 10, 10));
        line2.addEdge(new Edge(4L, 5L, 10, 10));

        line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22,30), 10);
        line3.addEdge(new Edge(null, 5L, 0, 0));
        line3.addEdge(new Edge( 5L, 6L, 10, 10));
        line3.addEdge(new Edge(6L, 3L, 1000, 1));
    }

    @DisplayName("같은 호선 내에서의 경로 찾기 수행")
    @Test
    void findPathInSameLine() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6));

        PathResponse pathResponse = pathService.calculatePath(
                new PathRequest("강남역", "선릉역", PathType.DISTANCE));

        assertThat(pathResponse.getDistance()).isEqualTo(15);
        assertThat(pathResponse.getDuration()).isEqualTo(25);
        assertThat(pathResponse.getStations().get(0).getName()).isEqualTo("강남역");
        assertThat(pathResponse.getStations().get(1).getName()).isEqualTo("역삼역");
        assertThat(pathResponse.getStations().get(2).getName()).isEqualTo("선릉역");
    }

    @DisplayName("다른 호선 내에서의 경로 찾기 수행")
    @Test
    void findPathInDifferentLine() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6));

        PathResponse pathResponse = pathService.calculatePath(
                new PathRequest("선릉역", "양재시민의숲역", PathType.DISTANCE));

        assertThat(pathResponse.getDistance()).isEqualTo(35);
        assertThat(pathResponse.getDuration()).isEqualTo(45);
        assertThat(pathResponse.getStations().get(0).getName()).isEqualTo("선릉역");
        assertThat(pathResponse.getStations().get(1).getName()).isEqualTo("역삼역");
        assertThat(pathResponse.getStations().get(2).getName()).isEqualTo("강남역");
        assertThat(pathResponse.getStations().get(3).getName()).isEqualTo("양재역");
        assertThat(pathResponse.getStations().get(4).getName()).isEqualTo("양재시민의숲역");
    }

    @DisplayName("시간 기준으로 경로 찾기 수행")
    @Test
    void findPathByDuration() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6));

        PathResponse pathResponse = pathService.calculatePath(
                new PathRequest("선릉역", "양재시민의숲역", PathType.DURATION));

        assertThat(pathResponse.getDistance()).isEqualTo(1010);
        assertThat(pathResponse.getDuration()).isEqualTo(11);
        assertThat(pathResponse.getStations().get(0).getName()).isEqualTo("선릉역");
        assertThat(pathResponse.getStations().get(1).getName()).isEqualTo("신촌역");
        assertThat(pathResponse.getStations().get(2).getName()).isEqualTo("양재시민의숲역");
    }


    @DisplayName("출발지와 도착지가 같은 경우 예외가 발생하는지 테스트")
    @Test
    void sameSourceAndTargetTest() {
        PathRequest pathRequest = new PathRequest("강남역", "강남역", PathType.DISTANCE);

        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6));

        assertThatThrownBy(() -> pathService.calculatePath(pathRequest))
                .isInstanceOf(WrongPathException.class)
                .hasMessage("잘못된 입력입니다.");
    }

    @DisplayName("출발지와 도착지가 연결되어 있지 않은 경우 예외가 발생하는지 테스트")
    @Test
    void notConnectTest() {
        Line line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Station station6 = new Station(6L, "까치산역");

        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6));

        PathRequest pathRequest = new PathRequest("강남역", "까치산역", PathType.DISTANCE);

        assertThatThrownBy(() -> pathService.calculatePath(pathRequest))
                .isInstanceOf(WrongPathException.class)
                .hasMessage("잘못된 입력입니다.");
    }

    @DisplayName("입력된 역을 찾을 수 없는 경우 예외가 발생하는지 테스트")
    @Test
    void stationNotFoundTest() {
        PathRequest pathRequest = new PathRequest("강남역", "제주역", PathType.DISTANCE);

        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5, station6));

        assertThatThrownBy(() -> pathService.calculatePath(pathRequest))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("존재하지 않는 역입니다.");
    }
}
