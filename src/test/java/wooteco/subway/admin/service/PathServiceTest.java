package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME_GANGNAM = "강남역";
    private static final String STATION_NAME_YEOKSAM = "역삼역";
    private static final String STATION_NAME_SEOLLUNG = "선릉역";
    private static final String STATION_NAME_SAMSEONG = "삼성역";
    private static final String STATION_NAME_GANGNAM_OFFICE = "강남구쳥역";
    private static final String STATION_NAME_SEONJEONGLUNG = "선정릉역";
    private static final String STATION_NAME_HANTI = "한티역";

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private PathService pathService;

    private Line line2;
    private Line bundangLine;

    private Station gangnam;
    private Station yeoksam;
    private Station seollung;
    private Station samsong;
    private Station gangnamOffice;
    private Station seonjeonglung;
    private Station hanti;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineRepository, stationRepository);
        gangnam = new Station(1L, STATION_NAME_GANGNAM);
        yeoksam = new Station(2L, STATION_NAME_YEOKSAM);
        seollung = new Station(3L, STATION_NAME_SEOLLUNG);
        samsong = new Station(4L, STATION_NAME_SAMSEONG);
        gangnamOffice = new Station(5L, STATION_NAME_GANGNAM_OFFICE);
        seonjeonglung = new Station(6L, STATION_NAME_SEONJEONGLUNG);
        hanti = new Station(7L, STATION_NAME_HANTI);

        line2 = new Line(1L, "2호선", "bg-gray-300", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        bundangLine = new Line(2L, "분당선", "bg-gray-300", LocalTime.of(05, 00), LocalTime.of(23, 30), 7);

        line2.addLineStation(new LineStation(null, 1L, 10, 10));
        line2.addLineStation(new LineStation(1L, 2L, 10, 10));
        line2.addLineStation(new LineStation(2L, 3L, 10, 10));
        line2.addLineStation(new LineStation(3L, 4L, 10, 10));

        bundangLine.addLineStation(new LineStation(null, 5L, 10, 10));
        bundangLine.addLineStation(new LineStation(5L, 6L, 10, 10));
        bundangLine.addLineStation(new LineStation(6L, 3L, 10, 10));
        bundangLine.addLineStation(new LineStation(3L, 7L, 10, 10));
    }

    @Test
    void findShortestDistancePath() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line2, bundangLine));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(gangnam, yeoksam, seollung, samsong, gangnamOffice, seonjeonglung, hanti));

        PathResponse response = pathService.calculatePath(STATION_NAME_GANGNAM, STATION_NAME_HANTI, "DISTANCE");
        assertThat(response.getStations().size()).isEqualTo(4);
        assertThat(response.getStations().get(0).getName()).isEqualTo(STATION_NAME_GANGNAM);
        assertThat(response.getStations().get(1).getName()).isEqualTo(STATION_NAME_YEOKSAM);
        assertThat(response.getStations().get(2).getName()).isEqualTo(STATION_NAME_SEOLLUNG);
        assertThat(response.getStations().get(3).getName()).isEqualTo(STATION_NAME_HANTI);
        assertThat(response.getDistance()).isEqualTo(30L);
        assertThat(response.getDuration()).isEqualTo(30L);
    }

    @Test
    void findShortestDistancePath2() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line2, bundangLine));
        when(stationRepository.findAll()).thenReturn(Arrays.asList(gangnam, yeoksam, seollung, samsong, gangnamOffice, seonjeonglung, hanti));

        PathResponse response = pathService.calculatePath(STATION_NAME_HANTI, STATION_NAME_GANGNAM, "DISTANCE");
        assertThat(response.getStations().size()).isEqualTo(4);
        assertThat(response.getStations().get(0).getName()).isEqualTo(STATION_NAME_HANTI);
        assertThat(response.getStations().get(1).getName()).isEqualTo(STATION_NAME_SEOLLUNG);
        assertThat(response.getStations().get(2).getName()).isEqualTo(STATION_NAME_YEOKSAM);
        assertThat(response.getStations().get(3).getName()).isEqualTo(STATION_NAME_GANGNAM);
        assertThat(response.getDistance()).isEqualTo(30L);
        assertThat(response.getDuration()).isEqualTo(30L);
    }
}
