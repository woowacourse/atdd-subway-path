package wooteco.subway.admin.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class GraphServiceTest {
    private final GraphService graphService = new GraphService(new DijkstraService());

    @Test
    void findPath() {
        //given
        LineStation lineStation1 = new LineStation(null, 1L, 10, 10);
        LineStation lineStation2 = new LineStation(1L, 2L, 10, 10);
        LineStation lineStation3 = new LineStation(2L, 5L, 10, 10);
        LineStation lineStation4 = new LineStation(1L, 3L, 10, 10);
        LineStation lineStation5 = new LineStation(3L, 4L, 10, 10);
        LineStation lineStation6 = new LineStation(4L, 5L, 10, 10);

        Line from = new Line(1L, "1호선", LocalTime.now(), LocalTime.now(), 10);
        from.addLineStation(lineStation1);
        from.addLineStation(lineStation2);
        from.addLineStation(lineStation3);

        Line to = new Line(2L, "2호선", LocalTime.now(), LocalTime.now(), 10);
        to.addLineStation(lineStation1);
        to.addLineStation(lineStation4);
        to.addLineStation(lineStation5);
        to.addLineStation(lineStation6);
        List<Line> lines = new ArrayList<>(Arrays.asList(from, to));

        //when
        List<Long> path = graphService.findPath(lines, 1L, 5L, PathType.DISTANCE);

        //then
        assertThat(path.size()).isEqualTo(3);
    }
}