package wooteco.subway.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DijkstraShortestPathTest {

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        Station 강남역 = new Station(1L, "강남역");
        Station 잠실역 = new Station(2L, "잠실역");
        Station 왕십리역 = new Station(3L, "왕십리역");
        graph.addVertex(강남역);
        graph.addVertex(잠실역);
        graph.addVertex(왕십리역);
        graph.setEdgeWeight(graph.addEdge(강남역, 잠실역), 2);
        graph.setEdgeWeight(graph.addEdge(잠실역, 왕십리역), 2);
        graph.setEdgeWeight(graph.addEdge(강남역, 왕십리역), 100);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<Station> shortestPath
                = dijkstraShortestPath.getPath(왕십리역, 강남역).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat((int)dijkstraShortestPath.getPathWeight(왕십리역, 강남역)).isEqualTo(4);
    }
}
