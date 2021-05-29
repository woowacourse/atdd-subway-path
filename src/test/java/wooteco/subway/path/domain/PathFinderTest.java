package wooteco.subway.path.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

@DisplayName("pathFinder 테스트")
public class PathFinderTest {

    private SubwayWeightedMultiGraph subwayWeightedMultiGraph;
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;

    @BeforeEach
    public void setUp() {
        station1 = new Station("1");
        station2 = new Station("2");
        station3 = new Station("3");
        station4 = new Station("4");
        station5 = new Station("5");
        station6 = new Station("6");

        List<Section> sections = new LinkedList<>();
        sections.add(new Section(station6, station5, 9));
        sections.add(new Section(station5, station4, 6));
        sections.add(new Section(station4, station2, 15));
        sections.add(new Section(station1, station2, 7));
        sections.add(new Section(station1, station6, 14));
        sections.add(new Section(station6, station3, 2));
        sections.add(new Section(station3, station4, 11));
        sections.add(new Section(station3, station2, 10));
        sections.add(new Section(station3, station1, 9));

        subwayWeightedMultiGraph = new SubwayWeightedMultiGraph(sections);
    }

    @DisplayName("지하철 노선 구간 그래프의 두 정점에 대한 최단 경로를 구한다.")
    @Test
    public void shortestPath() {
        // given
        PathFinder pathFinder = new PathFinder(
            new DijkstraShortestPath<>(subwayWeightedMultiGraph.getGraph()));

        // when then
        assertThat(pathFinder.shortestPath(station1, station5))
            .containsExactly(station1, station3, station6, station5);
    }

    @DisplayName("지하철 노선 구간 그래프의 두 정점에 대한 최단 거리를 구한다.")
    @Test
    public void shortestPathDistance() {
        // given
        PathFinder pathFinder = new PathFinder(
            new DijkstraShortestPath<>(subwayWeightedMultiGraph.getGraph()));

        // when then
        assertThat(pathFinder.shortestPathDistance(station1, station5)).isEqualTo(20);
    }


    @DisplayName("다양한 알고리즘을 사용해여 지하철 노선 구간 그래프의 두 정점에 대한 최단 경로를 구한다.")
    @Test
    public void shortestPathByAlgorithms() {
        // given
        PathFinder dijkstraShortestPathFinder = new PathFinder(new DijkstraShortestPath<>(subwayWeightedMultiGraph.getGraph()));
        PathFinder bellmanFordShortestPathFinder = new PathFinder(new BellmanFordShortestPath<>(subwayWeightedMultiGraph.getGraph()));
        PathFinder floydWarshallShortestPathsFinder = new PathFinder(new FloydWarshallShortestPaths<>(subwayWeightedMultiGraph.getGraph()));
        List<Station> expectedPath = Arrays.asList(station1, station3, station6, station5);

        // when then
        assertThat(dijkstraShortestPathFinder.shortestPath(station1, station5)).isEqualTo(expectedPath);
        assertThat(bellmanFordShortestPathFinder.shortestPath(station1, station5)).isEqualTo(expectedPath);
        assertThat(floydWarshallShortestPathsFinder.shortestPath(station1, station5)).isEqualTo(expectedPath);
    }

    @DisplayName("다양한 알고리즘을 사용해여 지하철 노선 구간 그래프의 두 정점에 대한 최단 거리를 구한다.")
    @Test
    public void shortestDistanceByAlgorithms() {
        // given
        PathFinder dijkstraShortestPathFinder = new PathFinder(new DijkstraShortestPath<>(subwayWeightedMultiGraph.getGraph()));
        PathFinder bellmanFordShortestPathFinder = new PathFinder(new BellmanFordShortestPath<>(subwayWeightedMultiGraph.getGraph()));
        PathFinder floydWarshallShortestPathsFinder = new PathFinder(new FloydWarshallShortestPaths<>(subwayWeightedMultiGraph.getGraph()));

        // when then
        assertThat(dijkstraShortestPathFinder.shortestPathDistance(station1, station5)).isEqualTo(20);
        assertThat(bellmanFordShortestPathFinder.shortestPathDistance(station1, station5)).isEqualTo(20);
        assertThat(floydWarshallShortestPathsFinder.shortestPathDistance(station1, station5)).isEqualTo(20);
    }
}