package wooteco.subway.admin.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LinesTest {

    private Lines lines;

    @BeforeEach
    void setUp() {
        Line line1 = new Line(1L, "1호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line1.addEdge(new Edge(null, 1L, 10, 10));
        line1.addEdge(new Edge(1L, 2L, 10, 10));
        line1.addEdge(new Edge(2L, 3L, 10, 10));

        Line line2 = new Line(2L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line2.addEdge(new Edge(null, 1L, 10, 10));
        line2.addEdge(new Edge(1L, 4L, 10, 10));
        line2.addEdge(new Edge(4L, 5L, 10, 10));

        lines = new Lines(Arrays.asList(line1, line2));
    }

    @DisplayName("모든 역의 id를 중복없이 잘 가져오는지 테스트")
    @Test
    void getStationIdsTest() {
        assertThat(lines.getStationIds().size()).isEqualTo(5);
        assertThat(lines.getStationIds()).containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L);
    }
}
