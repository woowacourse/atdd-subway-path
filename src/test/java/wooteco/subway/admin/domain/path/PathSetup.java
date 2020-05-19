package wooteco.subway.admin.domain.path;

import org.junit.jupiter.api.BeforeEach;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathSetup {
    protected List<LineStation> lineStations;

    @BeforeEach
    void setup() {
        Line line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 0, 0));
        line1.addLineStation(new LineStation(1L, 2L, 50, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));

        Line line2 = new Line(2L, "8호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 1L, 0, 0));
        line2.addLineStation(new LineStation(1L, 2L, 10, 50));
        line2.addLineStation(new LineStation(2L, 6L, 10, 10));


        Line line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line3.addLineStation(new LineStation(null, 9L, 0, 0));

        lineStations = Stream.of(line1, line2, line3)
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
