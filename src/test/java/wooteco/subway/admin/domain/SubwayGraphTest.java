package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.GraphResultResponse;

class SubwayGraphTest {
    @DisplayName("최딘거리를 찾아 Response를 리턴하는 테스트")
    @Test
    void findShortestPath() {
        Line line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line line3 = new Line(3L, "3호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        line1.addLineStation(new LineStation(null, 1L, 0, 10));
        line1.addLineStation(new LineStation(1L, 2L, 1, 10));
        line1.addLineStation(new LineStation(2L, 3L, 10, 10));

        line2.addLineStation(new LineStation(null, 4L, 0, 10));
        line2.addLineStation(new LineStation(4L, 2L, 10, 10));
        line2.addLineStation(new LineStation(2L, 5L, 1, 10));

        line3.addLineStation(new LineStation(null, 5L, 0, 10));
        line3.addLineStation(new LineStation(5L, 6L, 1, 10));
        line3.addLineStation(new LineStation(6L, 3L, 1, 10));

        translationGraph subwayGraph = new SubwayGraph(Arrays.asList(line1, line2, line3), CriteriaType.DISTANCE);

        Long source = 3L;
        Long target = 1L;

        GraphResultResponse shortestPath = subwayGraph.findShortestPath(source, target);

        assertThat(shortestPath.getStationIds().size()).isEqualTo(5);
        assertThat(shortestPath.getDistance()).isEqualTo(4);
        assertThat(shortestPath.getDuration()).isEqualTo(40);
    }
}