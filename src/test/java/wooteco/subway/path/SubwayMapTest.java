package wooteco.subway.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.domain.SubwayMap;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {
    @DisplayName("SubwayMap의 calculateShortestPath가 최단 경로를 반환한다.")
    @Test
    public void calculateShortestPath() {
        //given
        final Station station1 = new Station(1L, "v1");
        final Station station2 = new Station(2L, "v2");
        final Station station3 = new Station(3L, "v3");
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex(station1);
        graph.addVertex(station2);
        graph.addVertex(station3);
        graph.setEdgeWeight(graph.addEdge(station1, station2), 2);
        graph.setEdgeWeight(graph.addEdge(station2, station3), 2);
        graph.setEdgeWeight(graph.addEdge(station1, station3), 100);
        final SubwayMap subwayMap = new SubwayMap(graph);

        //when
        final Map<List<Station>, Double> shortestPath = subwayMap.calculateShortestPath(station3, station1);

        //then
        final List<Station> shortestPathStations = shortestPath.keySet().iterator().next();
        assertThat(shortestPathStations).isEqualTo(Arrays.asList(station3, station2, station1));
        final Double distance = shortestPath.get(shortestPathStations);
        assertThat(distance).isEqualTo(4);
    }
}
