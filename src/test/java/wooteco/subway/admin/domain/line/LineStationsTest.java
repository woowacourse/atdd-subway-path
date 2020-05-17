package wooteco.subway.admin.domain.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;
import wooteco.subway.admin.domain.line.path.SubwayGraph;
import wooteco.subway.admin.domain.line.path.SubwayRoute;

class LineStationsTest {
    private LineStation lineStation;
    private LineStation lineStation1;
    private LineStation lineStation2;
    private LineStations lineStations;

    @BeforeEach
    void setUp() {
        lineStation = new LineStation(null, 1L, 5, 10);
        lineStation1 = new LineStation(1L, 2L, 5, 10);
        lineStation2 = new LineStation(2L, 3L, 5, 10);
        lineStations = new LineStations(Sets.newHashSet(lineStation, lineStation1, lineStation2));
    }

    @Test
    void constructor() {
        assertThat(lineStations).isInstanceOf(LineStations.class);
    }

    @Test
    void toGraph() {
        assertThat(lineStations.toGraph((graph, edge) -> graph.setEdgeWeight(edge, edge.getDistance()))).isInstanceOf(SubwayGraph.class);
    }
}