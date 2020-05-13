package wooteco.subway.admin.domain.line.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;
import wooteco.subway.admin.domain.line.LineStation;
import wooteco.subway.admin.domain.line.LineStations;

class SubwayRouteFactoryTest {

    @Test
    void create() {
        LineStation lineStation = new LineStation(null, 1L, 5, 10);
        LineStation lineStation1 = new LineStation(1L, 2L, 5, 10);
        LineStation lineStation2 = new LineStation(2L, 3L, 5, 10);
        LineStations lineStations = new LineStations(Sets.newHashSet(lineStation, lineStation1, lineStation2));

        SubwayRouteFactory distanceRoute = new SubwayRouteFactory((graph, edge)
            -> graph.setEdgeWeight(edge, edge.getDistance()));

        SubwayRouteFactory durationRoute = new SubwayRouteFactory((graph, edge)
            -> graph.setEdgeWeight(edge, edge.getDuration()));

        assertThat(distanceRoute.create(lineStations, 1L, 3L)).isInstanceOf(SubwayRoute.class);
        assertThat(durationRoute.create(lineStations, 1L, 3L)).isInstanceOf(SubwayRoute.class);
    }
}