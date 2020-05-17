package wooteco.subway.admin.domain.line.path;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.line.LineStation;
import wooteco.subway.admin.domain.line.LineStations;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {
    private LineStation lineStation;
    private LineStation lineStation1;
    private LineStation lineStation2;
    private LineStations lineStations;
    private SubwayGraph subwayGraph;

    @BeforeEach
    void setUp() {
        lineStation = new LineStation(null, 1L, 5, 10);
        lineStation1 = new LineStation(1L, 2L, 5, 10);
        lineStation2 = new LineStation(2L, 3L, 5, 10);
        lineStations = new LineStations(Sets.newHashSet(lineStation, lineStation1, lineStation2));
        subwayGraph = lineStations.toGraph((graph, edge) -> graph.setEdgeWeight(edge, edge.getDistance()));
    }

    @Test
    void constructor() {
        assertThat(subwayGraph).isInstanceOf(SubwayGraph.class);
    }

    @Test
    void findShortestPath() {
        assertThat(subwayGraph.findDijkstraShortestPath(1L, 3L)).isInstanceOf(SubwayRoute.class);
    }
}