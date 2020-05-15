package wooteco.subway.admin.domain;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.admin.exception.IllegalPathRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayGraphsTest {

    @DisplayName("찾은 경로중에 가장 조건에 맞는 최적 경로를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"DISTANCE,2,11", "DURATION,11,2"})
    void getPath(SubwayGraphKey key, Integer expectDistance, Integer expectDuration) {
        //given
        Edge edge1 = new Edge(1L, 2L, 1, 1);
        Edge edge2 = new Edge(2L, 3L, 10, 1);

        Edge edge3 = new Edge(2L, 3L, 1, 10);

        SubwayGraphs subwayGraphs = new SubwayGraphs(Sets.newLinkedHashSet(edge1, edge2, edge3));

        //when
        PathDetail path = subwayGraphs.getPath(1L, 3L, key);

        //then
        assertThat(path.getTotalDistance()).isEqualTo(expectDistance);
        assertThat(path.getTotalDuration()).isEqualTo(expectDuration);
    }

    @DisplayName("그래프에 존재하지 않는 최단 거리 경로를 탐색할 경우 Exception 발생")
    @ParameterizedTest
    @CsvSource(value = {"3,2", "1,3", "3,4"})
    void getPathException(Long source, Long target) {
        //given
        Edge edge1 = new Edge(null, 1L, 10, 10);
        Edge edge2 = new Edge(1L, 2L, 10, 10);

        SubwayGraphs subwayGraphs = new SubwayGraphs(Sets.newLinkedHashSet(edge1, edge2));

        //when
        assertThatThrownBy(() -> subwayGraphs.getPath(source, target, SubwayGraphKey.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d - %d : 해당 역이 존재하지 않습니다.", source, target);
    }


    @DisplayName("존재하지 않는 경로를 조회하는 경우 Exception 발생")
    @Test
    void getPathNotFound() {
        //given
        Edge edge = new Edge(null, 1L, 1, 1);
        Edge edge1 = new Edge(1L, 2L, 1, 1);
        Edge edge2 = new Edge(2L, 3L, 10, 1);
        Edge edge3 = new Edge(4L, 5L, 1, 10);

        SubwayGraph subwayGraph = new SubwayGraph(Sets.newLinkedHashSet(edge, edge1, edge2, edge3), Edge::getDistance);

        //then
        assertThatThrownBy(() -> subwayGraph.getPath(1L, 5L))
                .isInstanceOf(IllegalPathRequestException.class)
                .hasMessage("%d - %d : 존재하지 않는 경로입니다.", 1L, 5L);

    }
}