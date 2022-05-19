package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PathGraphTest {

    @DisplayName("findShortestPath 메서드는 최단경로를 찾는다")
    @Nested
    class FindShortestPath {

        @Test
        void 최단경로를_찾는다() {
            Station 강남역 = new Station("강남역");
            Station 양재역 = new Station("양재역");
            Station 양재시민의숲역 = new Station("양재시민의숲역");
            Section section1 = new Section(강남역, 양재시민의숲역, 10);
            Section section2 = new Section(강남역, 양재역, 3);
            Section section3 = new Section(양재역, 양재시민의숲역, 3);
            Line line1 = new Line("1호선", "파란색");
            Line line2 = new Line("2호선", "초록색");
            line1.addSection(section1);
            line2.addSection(section2);
            line2.addSection(section3);

            List<Line> lines = List.of(line1, line2);
            PathGraph pathGraph = new PathGraph(lines);
            Path expected = new Path(List.of(강남역, 양재역, 양재시민의숲역), 6);

            assertThat(pathGraph.findShortestPath(강남역, 양재시민의숲역)).isEqualTo(expected);
        }

        @Test
        void section에_등록되지_않는_출발역으로_최단경로를_찾는경우_예외발생() {
            Station 강남역 = new Station("강남역");
            Station 양재역 = new Station("양재역");
            Station 양재시민의숲역 = new Station("양재시민의숲역");
            Section section = new Section(강남역, 양재시민의숲역, 10);
            Line line = new Line("1호선", "파란색");
            line.addSection(section);

            PathGraph pathGraph = new PathGraph(List.of(line));

            assertThatThrownBy(() -> pathGraph.findShortestPath(양재역, 양재시민의숲역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("graph must contain the source vertex");
        }

        @Test
        void section에_등록되지_않는_도착역으로_최단경로를_찾는경우_예외발생() {
            Station 강남역 = new Station("강남역");
            Station 양재역 = new Station("양재역");
            Station 양재시민의숲역 = new Station("양재시민의숲역");
            Section section = new Section(강남역, 양재시민의숲역, 10);
            Line line = new Line("1호선", "파란색");
            line.addSection(section);

            PathGraph pathGraph = new PathGraph(List.of(line));

            assertThatThrownBy(() -> pathGraph.findShortestPath(강남역, 양재역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("graph must contain the sink vertex");
        }
    }
}
