package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.path.PathType;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {

    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private LineService lineService;

    private Line line1;
    private Line line2;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository, stationRepository);

        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);

        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2 = new Line(2L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new Edge(null, 1L, 0, 0));
        line1.addLineStation(new Edge(1L, 2L, 10, 5));
        line1.addLineStation(new Edge(2L, 3L, 10, 5));

        line2.addLineStation(new Edge(null, 4L, 10, 10));
    }

    @Test
    void addLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));

        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10,
            10);
        lineService.addLineStation(line1.getId(), request);

        assertThat(line1.getStations()).hasSize(4);

        List<Long> stationIds = line1.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationBetweenTwo() {
        when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));

        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line1.getId(), request);

        assertThat(line1.getStations()).hasSize(4);

        List<Long> stationIds = line1.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));

        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line1.getId(), request);

        assertThat(line1.getStations()).hasSize(4);

        List<Long> stationIds = line1.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
    }

    @Test
    void removeLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));
        lineService.removeLineStation(line1.getId(), 1L);

        assertThat(line1.getStations()).hasSize(2);

        List<Long> stationIds = line1.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationBetweenTwo() {
        when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));
        lineService.removeLineStation(line1.getId(), 2L);

        List<Long> stationIds = line1.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line1.getId())).thenReturn(Optional.of(line1));
        lineService.removeLineStation(line1.getId(), 3L);

        assertThat(line1.getStations()).hasSize(2);

        List<Long> stationIds = line1.getStationIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }

    @Test
    void findLineWithStationsById() {
        List<Station> stations = Lists
            .newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(3L, "삼성역"));
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line1));
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        LineDetailResponse lineDetailResponse = lineService.findDetailLineById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }

    @Test
    void findDetailLines() {
        List<Line> lines = Lists.newArrayList(line1, line2);
        when(lineRepository.findAll()).thenReturn(lines);

        List<Station> stations1 = Lists.newArrayList(station1, station2, station3);
        when(stationRepository.findAllById(line1.getStationIds())).thenReturn(stations1);
        List<Station> stations2 = Lists.newArrayList(station4, station2);
        when(stationRepository.findAllById(line2.getStationIds())).thenReturn(stations2);

        List<LineDetailResponse> response = lineService.findDetailLines();

        assertThat(response).hasSize(lines.size());
        assertThat(response.get(0).getStations()).hasSize(stations1.size());
        assertThat(response.get(1).getStations()).hasSize(stations2.size());

        assertThat(response.get(0).getStations().get(0).getId()).isEqualTo(station1.getId());
        assertThat(response.get(0).getStations().get(1).getId()).isEqualTo(station2.getId());
        assertThat(response.get(0).getStations().get(2).getId()).isEqualTo(station3.getId());
        assertThat(response.get(1).getStations().get(0).getId()).isEqualTo(station4.getId());
    }

    @ParameterizedTest
    @EnumSource(PathType.class)
    void findShortestPath(PathType pathType) {
        List<Line> lines = Lists.newArrayList(line1);
        List<Station> stations = Lists.newArrayList(station1, station2, station3, station4);
        List<String> names = stations.stream().map(Station::getName).collect(Collectors.toList());

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findIdByName(station1.getName()))
                .thenReturn(Optional.of(station1.getId()));
        when(stationRepository.findIdByName(station3.getName()))
                .thenReturn(Optional.of(station3.getId()));
        when(stationRepository.findAllNameById(anyList())).thenReturn(names);

        PathResponse response = lineService
                .findShortestPath(station1.getName(), station3.getName(), pathType);

        assertThat(response.getDistance()).isEqualTo(20);
        assertThat(response.getDuration()).isEqualTo(10);
    }


    @DisplayName("최단거리, 최소시간이 같은 경로에서 이동 노선이 다를 때")
    @ParameterizedTest
    @MethodSource("generateTypePathArguments")
    void findShortestPath_DifferentLine(PathType pathType, int distance, int duration) {
        List<Line> lines = Lists.newArrayList(line1, line2);
        line2.addLineStation(new Edge(4L, 1L, 10, 5));
        line2.addLineStation(new Edge(1L, 2L, 5, 10));

        List<Station> stations = Lists.newArrayList(station1, station3);
        List<String> names = stations.stream().map(Station::getName).collect(Collectors.toList());

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findIdByName(station1.getName()))
                .thenReturn(Optional.of(station1.getId()));
        when(stationRepository.findIdByName(station3.getName()))
                .thenReturn(Optional.of(station3.getId()));
        when(stationRepository.findAllNameById(anyList())).thenReturn(names);

        PathResponse response = lineService.findShortestPath(STATION_NAME1, STATION_NAME3, pathType);
        assertThat(response.getDuration()).isEqualTo(duration);
        assertThat(response.getDistance()).isEqualTo(distance);
    }

    static Stream<Arguments> generateTypePathArguments() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE, 15, 15),
                Arguments.of(PathType.DURATION, 20, 10));
    }

    @DisplayName("(예외) 연결되지 않은 경로일 때")
    @Test
    void findShDistancePath_NotConnectedPath() {
        List<Line> lines = Lists.newArrayList(line1, line2);

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findIdByName(station1.getName()))
                .thenReturn(Optional.of(station1.getId()));
        when(stationRepository.findIdByName(station4.getName()))
                .thenReturn(Optional.of(station4.getId()));

        assertThatThrownBy(() -> lineService.findShortestPath(STATION_NAME1, STATION_NAME4, PathType.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 경로입니다.");
    }

    @DisplayName("(예외) 시작점과 도착점이 같은 경로일 때")
    @Test
    void findShortestDistancePath_EqualStation() {
        assertThatThrownBy(() -> lineService.findShortestPath(STATION_NAME1, STATION_NAME1, PathType.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같습니다.");
    }
}