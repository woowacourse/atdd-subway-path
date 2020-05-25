package wooteco.subway.admin.domain;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LinesTest {

    @DisplayName("Lines가 가지고 있는 Edges의 모든 Station id를 반환한다.")
    @Test
    void getStationIds() {
        //given
        Line line1 = new Line(1L, "", LocalTime.of(10, 10), LocalTime.of(10, 10), 10);
        line1.addEdge(new Edge(1L, 2L, 10, 10));
        Line line2 = new Line(2L, "", LocalTime.of(10, 10), LocalTime.of(10, 10), 10);
        line2.addEdge(new Edge(4L, 3L, 10, 10));

        Lines lines = new Lines(Lists.newArrayList(line1, line2));

        //when
        List<Long> stationIds = lines.getStationIds();

        //then
        assertThat(stationIds).containsExactly(2L, 3L);
    }
}