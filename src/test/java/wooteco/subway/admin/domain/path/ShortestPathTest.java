package wooteco.subway.admin.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortestPathTest {
    private Line line1;
    private Line line2;

    @BeforeEach
    void setUp() {
        line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addLineStation(new LineStation(null, 1L, 10, 10));
        line1.addLineStation(new LineStation(1L, 2L, 10, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));

        line2 = new Line(2L, "8호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addLineStation(new LineStation(null, 5L, 10, 10));
        line2.addLineStation(new LineStation(5L, 2L, 10, 10));
        line2.addLineStation(new LineStation(2L, 6L, 10, 10));
    }

    @DisplayName("전체 호선에 대한 그래프 생성 확인")
    @Test
    void createDistanceGraph() {
        List<LineStation> lineStations = Stream.of(line1, line2)
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        ShortestPath shortestPath = ShortestPath.of(lineStations, Type.DURATION);
        assertThat(shortestPath.getPath()).isInstanceOf(DijkstraShortestPath.class);
    }
}
