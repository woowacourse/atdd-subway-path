package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.domain.Station;

class PathTest {

    @Test
    @DisplayName("라이브러리 체크 테스트")
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
        List<String> shortestPath
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("최적 경로 반환 테스트")
    public void getOptimizedPath() {
        //given
        Station oneStation = new Station(1L,"v1");
        Station twoStation = new Station(2L, "v2");
        Station threeStation = new Station(3L, "v3");
        Section oneSection = new Section(1L, oneStation, twoStation, 2);
        Section twoSection = new Section(2L, twoStation, threeStation, 2);
        Sections newSections = new Sections(Arrays.asList(oneSection, twoSection));
        Line  line = new Line(1L, "1호선", "파란색", newSections);
        Path path = new Path(Arrays.asList(line));

        //when
        PathResponse optimizedPath = path.optimizedPath(threeStation, oneStation);

        //then
        assertThat(optimizedPath.getDistance()).isEqualTo(4);
        assertThat(optimizedPath.getStations().size()).isEqualTo(3);
    }
}