package wooteco.subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class GraphTest {

    @DisplayName("두 정점에 대한 최단경로를 가져온다.")
    @Test
    void getShortestPath() {
        Station 신림역 = new Station("신림역");
        Station 봉천역 = new Station("봉천역");
        Station 서울대입구역 = new Station("서울대입구역");

        Section section1 = new Section(신림역, 봉천역, 10);
        Section section2 = new Section(봉천역, 서울대입구역, 20);
        Section section3 = new Section(신림역, 서울대입구역, 100);

        Graph graph = new Graph();
        final Sections sections = new Sections(List.of(section1, section2, section3));
        graph.addSections(sections);
        ShortestPath path = graph.getShortestPath(서울대입구역, 신림역);


        assertAll(
                () -> assertThat(path.getVertexes()).containsExactly(서울대입구역, 봉천역, 신림역),
                () -> assertThat(path.getDistance()).isEqualTo(30)
        );
    }

    @DisplayName("두 정점 중 하나라도 경로에 존재하지 않으면 예외를 발생한다.")
    @Test
    void thrown_stationNotExist() {
        Station 신림역 = new Station("신림역");
        Station 봉천역 = new Station("봉천역");
        Station 서울대입구역 = new Station("서울대입구역");

        Section section1 = new Section(신림역, 봉천역, 10);

        Graph graph = new Graph();
        graph.addSections(new Sections(List.of(section1)));

        assertThatThrownBy(() -> graph.getShortestPath(서울대입구역, 신림역))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("지하철역이 존재하지 않습니다.");
    }

//    private List<Section> extracted(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath, List<Station> vertexList) {
//        List<Section> sectionList = new ArrayList<>();
//        for (int i = 0; i < vertexList.size() - 2; i++) {
//            final Station source1 = vertexList.get(i);
//            final Station sink = vertexList.get(i + 1);
//            final double pathWeight = dijkstraShortestPath.getPathWeight(source1, sink);
//            final Section foundSection = sections.getSections()
//                    .stream()
//                    .filter(section -> section.equals(new Section(source1, sink, (int) pathWeight)))
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException(""));
//            sectionList.add(foundSection);
//        }
//        return sectionList;
//    }
}
