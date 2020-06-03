package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    private PathService pathService;
    private Station kangnam;
    private Station yeoksam;
    private Station seolleung;
    private Station gyodae;
    private Station jamwon;
    private Station sinsa;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);

        seolleung = Station.of(2L, "선릉역");
        yeoksam = Station.of(3L, "역삼역");
        kangnam = Station.of(1L, "강남역");
        gyodae = Station.of(4L, "교대역");
        jamwon = Station.of(5L, "잠원역");
        sinsa = Station.of(6L, "신사역");
    }

    @DisplayName("출발역이나 도착역을 입력하지 않은 경우")
    @Test
    void findPathNullException() {
        assertThatThrownBy(() -> pathService.findPath(null, 1L, PathType.DURATION))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> pathService.findPath(null, null, PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역, 도착역이 같은 경우")
    @Test
    void findPathSameStationsException() {
        assertThatThrownBy(() -> pathService.findPath(1L, 1L, PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathNotExistException() {
        assertThatThrownBy(() -> pathService.findPath(1L, 7L, PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역, 도착역이 연결되어 있지 않은 경우")
    @Test
    void findPathNotConnectedException() {
        Line firstLine = Line.of(1L, "1호선", "bg-green-500",
            LocalTime.of(05, 30), LocalTime.of(23, 00), 10);
        firstLine.addLineStation(LineStation.of(null, jamwon.getId(), 10, 10));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(jamwon, yeoksam));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(firstLine));
        assertThatThrownBy(
            () -> pathService.findPath(jamwon.getId(), yeoksam.getId(), PathType.DISTANCE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> findPathSource() {
        return Stream.of(
            Arguments.of(PathType.DISTANCE, 6, 5, 5),
            Arguments.of(PathType.DURATION, 4, 3, 302)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "DISTANCE, 6, 5, 5",
        "DURATION, 4, 3, 302"
    })
    void findByPathByCsvSource(PathType pathType, int stationSize, int duration, int distance) {
        List<Line> lines = createLinesIncludedLineStations();
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(jamwon, sinsa, gyodae, seolleung, yeoksam, kangnam));

        PathResponse durationResponse = pathService.findPath(1L, 5L, pathType);
        assertThat(durationResponse.getStations()).size().isEqualTo(stationSize);
        assertThat(durationResponse.getDuration()).isEqualTo(duration);
        assertThat(durationResponse.getDistance()).isEqualTo(distance);
    }

    @ParameterizedTest
    @MethodSource("findPathSource")
    void findByPathByMethodSource(PathType pathType, int stationSize, int duration, int distance) {
        List<Line> lines = createLinesIncludedLineStations();
        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findAll()).thenReturn(
            Arrays.asList(jamwon, sinsa, gyodae, seolleung, yeoksam, kangnam));

        PathResponse durationResponse = pathService.findPath(1L, 5L, pathType);
        assertThat(durationResponse.getStations()).size().isEqualTo(stationSize);
        assertThat(durationResponse.getDuration()).isEqualTo(duration);
        assertThat(durationResponse.getDistance()).isEqualTo(distance);
    }

    private List<Line> createLinesIncludedLineStations() {
        Line first = Line.of(1L, "1호선", "bg-pink-600", LocalTime.of(10, 00), LocalTime.of(12, 00),
            1);
        Line second = Line.of(2L, "2호선", "bg-pink-600", LocalTime.of(10, 00), LocalTime.of(12, 00),
            1);
        Line third = Line.of(3L, "3호선", "bg-pink-600", LocalTime.of(10, 00), LocalTime.of(12, 00),
            1);
        first.addLineStation(LineStation.of(null, 1L, 1, 1));
        first.addLineStation(LineStation.of(1L, 3L, 1, 1));
        first.addLineStation(LineStation.of(3L, 2L, 1, 1));
        first.addLineStation(LineStation.of(2L, 4L, 1, 1));
        second.addLineStation(LineStation.of(null, 4L, 1, 1));
        second.addLineStation(LineStation.of(4L, 6L, 1, 1));
        second.addLineStation(LineStation.of(6L, 5L, 1, 1));
        third.addLineStation(LineStation.of(null, 3L, 1, 1));
        third.addLineStation(LineStation.of(3L, 6L, 300, 1));

        return Arrays.asList(first, second, third);
    }
}