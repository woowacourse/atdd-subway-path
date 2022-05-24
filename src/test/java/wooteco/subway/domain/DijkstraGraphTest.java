package wooteco.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class DijkstraGraphTest {

    @DisplayName("두 정점에 대한 최단경로를 가져온다.")
    @Test
    void getShortestPath() {
        final Station 신림역 = new Station("신림역");
        final Station 봉천역 = new Station("봉천역");
        final Station 서울대입구역 = new Station("서울대입구역");

        final Section section1 = new Section(신림역, 봉천역, 10);
        final Section section2 = new Section(봉천역, 서울대입구역, 20);
        final Section section3 = new Section(신림역, 서울대입구역, 100);

        final Graph graph = DijkstraGraph.of(new Sections(List.of(section1, section2, section3)));
        final ShortestPath path = graph.getShortestPath(서울대입구역, 신림역);

        final Sections sections = path.getSections();
        assertAll(
                () -> assertThat(path.getVertexes()).containsExactly(서울대입구역, 봉천역, 신림역),
                () -> assertThat(sections.getSections()).containsExactly(section2, section1),
                () -> assertThat(path.getDistance()).isEqualTo(30)
        );
    }

    @DisplayName("두 정점 중 하나라도 경로에 존재하지 않으면 예외를 발생한다.")
    @Test
    void thrown_stationNotExist() {
        final Station 신림역 = new Station("신림역");
        final Station 봉천역 = new Station("봉천역");
        final Station 서울대입구역 = new Station("서울대입구역");

        final Section section = new Section(신림역, 봉천역, 10);

        final Graph graph = DijkstraGraph.of(new Sections(List.of(section)));

        assertThatThrownBy(() -> graph.getShortestPath(서울대입구역, 신림역))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("지하철역이 존재하지 않습니다.");
    }
}
