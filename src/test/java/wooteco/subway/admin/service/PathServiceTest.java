package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.DijkstraPath;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.InvalidSubwayPathException;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

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
        pathService = new PathService(lineRepository, stationRepository, new DijkstraPath());
        line1 = new Line(1L, "1호선", LocalTime.of(5, 30), LocalTime.of(5, 30), 10, "bg-teal-500");
        line2 = new Line(2L, "2호선", LocalTime.of(5, 30), LocalTime.of(5, 30), 10, "bg-teal-300");

        station1 = new Station(1L, "1역");
        station2 = new Station(2L, "2역");
        station3 = new Station(3L, "3역");
        station4 = new Station(4L, "4역");
        station5 = new Station(5L, "5역");

        line1.addLineStation(new LineStation(null, station1.getId(), 0, 0));
        line1.addLineStation(new LineStation(station1.getId(), station2.getId(), 10, 2));
        line1.addLineStation(new LineStation(station2.getId(), station3.getId(), 10, 0));
        line2.addLineStation(new LineStation(null, station2.getId(), 0, 0));
        line2.addLineStation(new LineStation(station2.getId(), station4.getId(), 2, 0));
        line2.addLineStation(new LineStation(station4.getId(), station5.getId(), 2, 2));
        line2.addLineStation(new LineStation(station5.getId(), station3.getId(), 2, 0));

    }

    @DisplayName("최단거리 경로 조회 테스트")
    @Test
    void getShortestDistancePath() {
        // mock repository methods
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(station1, station2, station3, station4, station5)
        );
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        // when
        PathResponse path = pathService.getPath(new PathRequest("1역", "3역", PathType.DISTANCE));

        // then
        assertThat(path.getStations()).hasSize(5);
        assertThat(path.getDistance()).isEqualTo(16);
        assertThat(path.getDuration()).isEqualTo(4);
    }

    @DisplayName("최단시간 경로 조회 테스트")
    @Test
    void getShortestDurationPath() {
        // mock repository method
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(station1, station2, station3, station4, station5)
        );
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        // when
        PathResponse path = pathService.getPath(new PathRequest("1역", "3역", PathType.DURATION));

        // then
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(20);
        assertThat(path.getDuration()).isEqualTo(2);
    }

    @DisplayName("출발역이나 도착역이 존재하지 않는 경우")
    @Test
    public void sourceOrTargetDoesNotExist() {
        assertThatThrownBy(() ->
            pathService.getPath(new PathRequest("존재하지 않는 역", "1역", PathType.DISTANCE))
        ).isInstanceOf(StationNotFoundException.class);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 처리 테스트")
    @Test
    public void sameSourceTargetTest() {
        // mock repository methods
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(station1, station2, station3, station4, station5));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2));

        assertThatThrownBy(() ->
            pathService.getPath(new PathRequest("1역", "1역", PathType.DISTANCE))
        ).isInstanceOf(InvalidSubwayPathException.class);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    public void sourceTargetNotConnected() {
        // given
        Line line3 = new Line(3L, "3호선", LocalTime.of(5, 30), LocalTime.of(5, 30), 10, "bg-blue-300");
        Station station6 = new Station(6L, "6역");
        line3.addLineStation(new LineStation(null, station6.getId(), 10, 10));

        // mock repository methods
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(station1, station2, station3, station4, station5, station6));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line1, line2, line3));

        assertThatThrownBy(() ->
            pathService.getPath(new PathRequest("1역", "6역", PathType.DISTANCE))
        ).isInstanceOf(InvalidSubwayPathException.class);
    }
}
