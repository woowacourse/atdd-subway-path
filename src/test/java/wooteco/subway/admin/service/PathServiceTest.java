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

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private static final String STATION_NAME1 = "강남역";
    private static final String STATION_NAME2 = "역삼역";
    private static final String STATION_NAME3 = "선릉역";
    private static final String STATION_NAME4 = "삼성역";
    private static final String STATION_NAME5 = "강남구쳥역";
    private static final String STATION_NAME6 = "선정릉역";
    private static final String STATION_NAME7 = "한티역";

    @Mock
    private LineRepository lineRepository;

    private PathService pathService;

    private Line line2;
    private Line bundangLine;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Station station7;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME1);
        station2 = new Station(2L, STATION_NAME2);
        station3 = new Station(3L, STATION_NAME3);
        station4 = new Station(4L, STATION_NAME4);
        station5 = new Station(2L, STATION_NAME5);
        station6 = new Station(3L, STATION_NAME6);
        station7 = new Station(4L, STATION_NAME7);

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

        PathResponse response = pathService.calculatePath(STATION_NAME1, STATION_NAME7, "DISTANCE");
        assertThat(response.getStations().get(0).getName()).isEqualTo(STATION_NAME1);
        assertThat(response.getStations().get(1).getName()).isEqualTo(STATION_NAME2);
        assertThat(response.getStations().get(2).getName()).isEqualTo(STATION_NAME3);
        assertThat(response.getStations().get(3).getName()).isEqualTo(STATION_NAME7);
        assertThat(response.getDistance()).isEqualTo(30L);
        assertThat(response.getDuration()).isEqualTo(30L);
    }
}
