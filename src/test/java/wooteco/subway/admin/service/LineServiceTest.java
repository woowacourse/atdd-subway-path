package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.exception.NoExistLineException;
import wooteco.subway.admin.exception.NoExistStationException;
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

        line = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-600");
        line.addLineStation(new LineStation(null, 1L, 10, 10));
        line.addLineStation(new LineStation(1L, 2L, 10, 10));
        line.addLineStation(new LineStation(2L, 3L, 10, 10));
    }

    @DisplayName("존재하지 않는 노선 조회")
    @Test
    void findLineWithInvalidId() {
        when(lineRepository.findById(anyLong())).thenThrow(
            new NoExistLineException("존재하지 않는 노선입니다."));

        assertThatThrownBy(() -> lineService.findLineById(2L))
            .isInstanceOf(NoExistLineException.class)
            .hasMessage("존재하지 않는 노선입니다.");
    }

    @DisplayName("존재하지 않는 역 조회")
    @Test
    void findStationWithInvalidName() {
        when(stationRepository.findByName(anyString())).thenThrow(
            new NoExistStationException("존재하지 않는 역입니다."));

        assertThatThrownBy(() -> lineService.findStationByName("서울역"))
            .isInstanceOf(NoExistStationException.class)
            .hasMessage("존재하지 않는 역입니다.");
    }

    @DisplayName("노선에 첫번째 역 추가")
    @Test
    void addLineStationAtTheFirstOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(null, station4.getId(), 10,
            10);
        lineService.addLineStation(line.getId(), request.toLineStation());

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getSortedLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(4L);
        assertThat(stationIds.get(1)).isEqualTo(1L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @DisplayName("노선 중간에 역 추가")
    @Test
    void addLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(station1.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request.toLineStation());

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getSortedLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(4L);
        assertThat(stationIds.get(2)).isEqualTo(2L);
        assertThat(stationIds.get(3)).isEqualTo(3L);
    }

    @DisplayName("노선 마지막에 역 추가")
    @Test
    void addLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        LineStationCreateRequest request = new LineStationCreateRequest(station3.getId(),
            station4.getId(), 10, 10);
        lineService.addLineStation(line.getId(), request.toLineStation());

        assertThat(line.getStations()).hasSize(4);

        List<Long> stationIds = line.getSortedLineStationsId();
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

        List<Long> stationIds = line.getSortedLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(2L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationBetweenTwo() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        lineService.removeLineStation(line.getId(), 2L);

        List<Long> stationIds = line.getSortedLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(3L);
    }

    @Test
    void removeLineStationAtTheEndOfLine() {
        when(lineRepository.findById(line.getId())).thenReturn(Optional.of(line));

        lineService.removeLineStation(line.getId(), 3L);

        assertThat(line.getStations()).hasSize(2);

        List<Long> stationIds = line.getSortedLineStationsId();
        assertThat(stationIds.get(0)).isEqualTo(1L);
        assertThat(stationIds.get(1)).isEqualTo(2L);
    }
}
