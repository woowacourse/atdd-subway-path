package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;

    private Line line2;
    private Line line7;
    private Line lineB;
    private List<Station> stations;

    @BeforeEach
    void setUP() {
        stations = Arrays.asList(new Station(1L, "왕십리"), new Station(2L, "한양대"), new Station(3L, "뚝섬"),
                new Station(4L, "성수"), new Station(5L, "건대입구"), new Station(6L, "뚝섬유원지"), new Station(7L, "청담"),
                new Station(8L, "강남구청"), new Station(9L, "압구정로데오"), new Station(10L, "서울숲"));


        line2 = new Line(1L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        line7 = new Line(2L, "7호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        lineB = new Line(3L, "분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 5, 2));
        line2.addLineStation(new LineStation(2L, 3L, 5, 2));
        line2.addLineStation(new LineStation(3L, 4L, 5, 2));
        line2.addLineStation(new LineStation(4L, 5L, 5, 2));

        line7.addLineStation(new LineStation(null, 5L, 0, 0));
        line7.addLineStation(new LineStation(5L, 6L, 7, 4));
        line7.addLineStation(new LineStation(6L, 7L, 7, 4));
        line7.addLineStation(new LineStation(7L, 8L, 7, 4));

        lineB.addLineStation(new LineStation(null, 8L, 0, 0));
        lineB.addLineStation(new LineStation(8L, 9L, 3, 1));
        lineB.addLineStation(new LineStation(9L, 10L, 3, 1));
        lineB.addLineStation(new LineStation(10L, 1L, 3, 1));
    }

    @Test
    @DisplayName("경로 조회 테스트")
    void findPathTest() {
        when(lineRepository.findAll()).thenReturn(Arrays.asList(line2, line7, lineB));
        when(stationRepository.findAll()).thenReturn(stations);
        PathService pathService = new PathService(lineRepository, stationRepository);
        PathResponse path = pathService.findPath(1L, 8L);

        System.out.println(path.getTotalDuration());
        assertThat(path.getPath().size()).isEqualTo(4);
    }
}
