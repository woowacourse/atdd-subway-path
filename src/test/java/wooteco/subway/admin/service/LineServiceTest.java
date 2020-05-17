package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
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

    private Line line;
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

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-gray-300");
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @Test
    void addLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        when(lineRepository.save(any())).thenReturn(line);

        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getLineStationsIds();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        when(lineRepository.save(any())).thenReturn(line);

        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(), station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getLineStationsIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @Test
    void addLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        when(lineRepository.save(any())).thenReturn(line);

        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(), station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request);

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getLineStationsIds();
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

        List<Long> stationIds = line.getLineStationsIds();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 2L);

        List<Long> stationIds = line.getLineStationsIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));
        lineService.removeLineStation(line.getId(), 3L);

        assertThat(line.getStations()).hasSize(2);

        List<Long> stationIds = line.getLineStationsIds();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }

    @Test
    void findLineWithStationsById() {
        List<Station> stations = Lists.newArrayList(new Station(1L, "강남역"), new Station(2L, "역삼역"), new Station(3L, "삼성역"));
        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
        when(stationRepository.findAllById(anyList())).thenReturn(stations);

        LineDetailResponse lineDetailResponse = lineService.findLineWithStationsById(1L);

        assertThat(lineDetailResponse.getStations()).hasSize(3);
    }
}
