package wooteco.subway.admin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.errors.PathException;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private GraphService graphService;

    @Test
    void findPath() {
        //given
        Station source = new Station("강남역");
        Station target = new Station("홍대입구");

        List<Long> path = Arrays.asList(1L, 2L, 5L);

        Line line = new Line(1L, "1호선", LocalTime.now(), LocalTime.now(), 10);
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 5L, 10, 10));

        List<Line> lines = Collections.singletonList(line);

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findByName(source.getName())).thenReturn(Optional.of(source));
        when(stationRepository.findByName(target.getName())).thenReturn(Optional.of(target));
        when(graphService.findPath(lines, source.getId(), target.getId(), PathType.DISTANCE)).thenReturn(path);
        when(stationRepository.findAllById(path)).thenReturn(Arrays.asList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(5L, "홍대입구")));

        PathService pathService = new PathService(stationRepository, lineRepository, graphService);

        //when
        PathResponse pathResponse = pathService.findPath(source.getName(), target.getName(), PathType.DISTANCE);

        //then
        assertThat(pathResponse.getStations().size()).isEqualTo(3);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
    }

    @Test
    void sameStationNameTest() {
        //given
        String source = "강남역";
        String target = "강남역";
        PathService pathService = new PathService(stationRepository, lineRepository, graphService);
        //when
        //then
        assertThatThrownBy(() -> pathService.findPath(source, target, PathType.DISTANCE))
                .isInstanceOf(PathException.class).hasMessage("출발역과 도착역은 같은 지하철역이 될 수 없습니다.");
    }

    @Test
    void sourceAndTargetNotLinked() {
        String source = "강남역";
        String target = "잠실역";
        Line line = new Line("1호선", LocalTime.now(), LocalTime.now(), 10);
        when(stationRepository.findByName(source)).thenReturn(Optional.of(new Station(source)));
        when(stationRepository.findByName(target)).thenReturn(Optional.of(new Station(target)));
        when(lineRepository.findAll()).thenReturn(Collections.singletonList(line));
        PathService pathService = new PathService(stationRepository, lineRepository, graphService);
        assertThatThrownBy(() -> pathService.findPath(source, target, PathType.DISTANCE))
                .isInstanceOf(PathException.class)
                .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");


    }

    @Test
    void sourceAndTargetNotfound() {
        String source = "강남역";
        String target = "잠실역";
        PathService pathService = new PathService(stationRepository, lineRepository, graphService);
        assertThatThrownBy(() -> pathService.findPath(source, target, PathType.DISTANCE))
                .isInstanceOf(PathException.class)
                .hasMessage("해당 역을 찾을 수 없습니다.");
    }

    @DisplayName("한글이외의 값이 입력될경우 익셉션이 발생한다")
    @Test
    void wrongInputExceptionTest() {
        String source = "gangNamyeok";
        String target = "잠실역";

        PathService pathService = new PathService(stationRepository, lineRepository, graphService);
        assertThatThrownBy(() -> pathService.findPath(source, target, PathType.DISTANCE))
                .isInstanceOf(PathException.class)
                .hasMessage("출발역과 도착역은 한글만 입력가능합니다.");
    }
}