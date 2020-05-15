package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.line.Line;
import wooteco.subway.admin.domain.line.LineStation;
import wooteco.subway.admin.domain.line.path.EdgeWeightType;
import wooteco.subway.admin.domain.line.path.NoPathException;
import wooteco.subway.admin.domain.station.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.PathResponses;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "도봉산역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private LineService lineService;

    private Line line;
    private List<Station> stations;
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

        stations = Arrays.asList(station1, station2, station3, station4);

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @Test
    void addLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10,
            10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
        assertThat(stationIds.get(2)).isEqualTo(3L);
        assertThat(stationIds.get(3)).isEqualTo(4L);
    }

    @Test
    void removeLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 1L);

        assertThat(line.getStations()).hasSize(2);

        List<Long> stationIds = line.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 2L);

        List<Long> stationIds = line.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 3L);

        assertThat(line.getStations()).hasSize(2);

        List<Long> stationIds = line.getLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }

    @Test
    void findLineWithStationsById() {
        List<Station> stations = Lists.newArrayList(new Station("강남역"), new Station("역삼역"),
            new Station("삼성역"));
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        LineDetailResponse lineDetailResponse = lineService.findLineWithStationsById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }

    @Test
    void findPath() {
        line.addLineStation(new LineStation(3L, 4L, 10, 10));
        when(lineRepository.findAllLineStations()).thenReturn(line.getStations());
        when(stationRepository.findAll()).thenReturn(stations);
        when(stationRepository.findByName(station1.getName())).thenReturn(Optional.of(station1));
        when(stationRepository.findByName(station4.getName())).thenReturn(Optional.of(station4));
        PathResponses response = lineService.findPathsBy(
            new PathRequest(station1.getName(), station4.getName()));
        PathResponse pathResponse = response.getResponse().get(EdgeWeightType.DISTANCE);
        assertThat(pathResponse.getTotalDuration()).isEqualTo(30);
        assertThat(pathResponse.getTotalDistance()).isEqualTo(30);
        assertThat(pathResponse.getStationResponses().size()).isEqualTo(4);
    }

    @Test
    void findPathWithException() {
        Line newLine = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        LineStation newLineStation = new LineStation(null, 4L, 10, 10);
        newLine.addLineStation(newLineStation);
        Set<LineStation> lineStations = new HashSet<>(line.getStations());
        lineStations.add(newLineStation);
        when(lineRepository.findAllLineStations()).thenReturn(lineStations);
        when(stationRepository.findByName(station1.getName())).thenReturn(Optional.of(station1));
        when(stationRepository.findByName(station4.getName())).thenReturn(Optional.of(station4));
        assertThatThrownBy(() -> lineService.findPathsBy(new PathRequest(station1.getName(), station4.getName())))
            .isInstanceOf(NoPathException.class)
            .hasMessage("경로가 존재하지 않습니다.");
    }

    @Test
    void wholeLines() {
        Line newLine = new Line(2L, "신분당선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        newLine.addLineStation(new LineStation(null, 4L, 10, 10));
        newLine.addLineStation(new LineStation(4L, 5L, 10, 10));
        newLine.addLineStation(new LineStation(5L, 6L, 10, 10));

        List<Station> stations1 = Arrays.asList(new Station(1L, "강남역"), new Station(2L, "역삼역"),
            new Station(3L, "삼성역"));
        List<Station> stations2 = Arrays.asList(new Station(4L, "양재역"), new Station(5L, "양재시민의숲역"),
            new Station(6L, "청계산입구역"));

        when(lineRepository.findAll()).thenReturn(Arrays.asList(this.line, newLine));
        when(stationRepository.findAllById(line.getLineStationsId())).thenReturn(stations1);
        when(stationRepository.findAllById(newLine.getLineStationsId())).thenReturn(stations2);

        List<LineDetailResponse> lineDetails = lineService.wholeLines().getLineDetailResponse();

        assertThat(lineDetails).isNotNull();
        assertThat(lineDetails.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetails.get(1).getStations().size()).isEqualTo(3);
    }

}
