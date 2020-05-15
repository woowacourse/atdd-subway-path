package wooteco.subway.admin.domain;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
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

    @DisplayName("구간에 station id 가 전부 포함되는지 확인")
    @ParameterizedTest
    @CsvSource(value = {"2,3,true", "4,5,false", "1,2,false", "3,4,false"})
    void containsStationIdAll(Long id1, Long id2, boolean expect) {
        //given
        Edge edge1 = new Edge(1L, 2L, 10, 10);
        Edge edge2 = new Edge(2L, 3L, 10, 10);
        Edges edges = new Edges(Sets.newLinkedHashSet(edge1, edge2));
        List<Long> stations = Arrays.asList(id1, id2);

        //when
        boolean isContain = edges.containsStationIdAll(stations);

        //then
        assertThat(isContain).isEqualTo(expect);
    }
}