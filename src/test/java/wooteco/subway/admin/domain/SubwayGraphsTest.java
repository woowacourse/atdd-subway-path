package wooteco.subway.admin.domain;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayGraphsTest {

    @DisplayName("찾은 경로중에 가장 최단 거리의 경로를 반환한다.")
    @Test
    void getShortestPath() {
        //given
        Edge edge1 = new Edge(null, 1L, 10, 10);
        Edge edge2 = new Edge(1L, 2L, 10, 10);
        SubwayGraph subwayGraph1 = new SubwayGraph(Sets.newLinkedHashSet(edge1, edge2));

        Edge edge3 = new Edge(null, 1L, 100, 10);
        Edge edge4 = new Edge(1L, 2L, 100, 10);
        SubwayGraph subwayGraph2 = new SubwayGraph(Sets.newLinkedHashSet(edge3, edge4));
        SubwayGraphs subwayGraphs = new SubwayGraphs(Arrays.asList(subwayGraph1, subwayGraph2));

        //when
        SubwayGraph shortestGraph = subwayGraphs.getShortestPath(1L, 2L);

        //then
        assertThat(shortestGraph).isEqualTo(subwayGraph1);
    }

    @DisplayName("그래프에 존재하지 않는 최단 거리 경로를 탐색할 경우 Exception 발생")
    @ParameterizedTest
    @CsvSource(value = {"3,2", "1,3", "3,4"})
    void getShortestPathException(Long source, Long target) {
        //given
        Edge edge1 = new Edge(null, 1L, 10, 10);
        Edge edge2 = new Edge(1L, 2L, 10, 10);
        SubwayGraph subwayGraph1 = new SubwayGraph(Sets.newLinkedHashSet(edge1, edge2));

        SubwayGraphs subwayGraphs = new SubwayGraphs(Collections.singletonList(subwayGraph1));

        //when
        assertThatThrownBy(() -> subwayGraphs.getShortestPath(source, target))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d - %d : 존재하지 않는 경로입니다.", source, target);
    }


}