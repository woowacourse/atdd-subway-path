package wooteco.subway.admin.domain;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EdgesTest {

    @DisplayName("Edges가 가지고 있는 station id 를 전부 반환한다.")
    @Test
    void getStationIds() {
        //given
        Edge edge1 = new Edge(1L, 2L, 10, 10);
        Edge edge2 = new Edge(2L, 3L, 10, 10);
        Edges edges = new Edges(Sets.newLinkedHashSet(edge1, edge2));

        //when
        List<Long> stationIds = edges.getStationIds();

        //then
        assertThat(stationIds).containsExactly(2L, 3L);
    }
}