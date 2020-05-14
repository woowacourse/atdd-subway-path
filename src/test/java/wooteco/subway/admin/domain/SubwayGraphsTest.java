package wooteco.subway.admin.domain;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayGraphsTest {

    @DisplayName("찾은 경로중에 가장 조건에 맞는 최적 경로를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"DISTANCE,2,11", "DURATION,11,2"})
    void getShortestPath(SubwayGraphs.SubwayGraphKey key, Integer expectDistance, Integer expectDuration) {
        //given
        Edge edge1 = new Edge(1L, 2L, 1, 1);
        Edge edge2 = new Edge(2L, 3L, 10, 1);

        Edge edge3 = new Edge(2L, 3L, 1, 10);

        SubwayGraphs subwayGraphs = new SubwayGraphs(Sets.newLinkedHashSet(edge1, edge2, edge3));

        //when
        Integer totalDistance = subwayGraphs.getTotalDistance(1L, 3L, key);
        Integer totalDuration = subwayGraphs.getTotalDuration(1L, 3L, key);

        //then
        assertThat(totalDistance).isEqualTo(expectDistance);
        assertThat(totalDuration).isEqualTo(expectDuration);
    }

    @DisplayName("그래프에 존재하지 않는 최단 거리 경로를 탐색할 경우 Exception 발생")
    @ParameterizedTest
    @CsvSource(value = {"3,2", "1,3", "3,4"})
    void getShortestPathException(Long source, Long target) {
        //given
        Edge edge1 = new Edge(null, 1L, 10, 10);
        Edge edge2 = new Edge(1L, 2L, 10, 10);

        SubwayGraphs subwayGraphs = new SubwayGraphs(Sets.newLinkedHashSet(edge1, edge2));

        //when
        assertThatThrownBy(() -> subwayGraphs.getPath(source, target, SubwayGraphs.SubwayGraphKey.DISTANCE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%d - %d : 존재하지 않는 경로입니다.", source, target);
    }


}