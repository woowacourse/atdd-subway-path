package wooteco.subway.admin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    PathService pathService;

    @Mock
    LineService lineService;

    List<Line> lines;
    List<Station> stations;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService);

        stations = Arrays.asList(
                new Station(1L, "환-강남역"),
                new Station(2L, "1-역삼역"),
                new Station(3L, "환-삼성역"),
                new Station(4L, "1-삼송역"),
                new Station(5L, "환-지축역"),
                new Station(6L, "2-역삼역"),
                new Station(7L, "2-삼송역"),
                new Station(8L, "3-역삼역"),
                new Station(9L, "3-삼송역")
        );

        Line line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        line1.addLineStation(new LineStation(null, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));
        line1.addLineStation(new LineStation(3L, 4L, 10, 10));
        line1.addLineStation(new LineStation(4L, 5L, 10, 10));

        line2.addLineStation(new LineStation(null, 1L, 20, 20));
        line2.addLineStation(new LineStation(1L, 6L, 20, 20));
        line2.addLineStation(new LineStation(6L, 3L, 20, 20));
        line2.addLineStation(new LineStation(3L, 7L, 20, 20));
        line2.addLineStation(new LineStation(7L, 5L, 20, 20));

        line3.addLineStation(new LineStation(null, 1L, 50, 50));
        line3.addLineStation(new LineStation(1L, 8L, 50, 50));
        line3.addLineStation(new LineStation(8L, 3L, 50, 50));
        line3.addLineStation(new LineStation(3L, 9L, 50, 50));
        line3.addLineStation(new LineStation(9L, 5L, 50, 50));

        lines = Arrays.asList(line1, line2, line3);
    }
}
