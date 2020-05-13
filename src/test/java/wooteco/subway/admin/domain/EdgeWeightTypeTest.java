package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EdgeWeightTypeTest {
    @Test
    void getFactory() {
        assertThat(EdgeWeightType.DISTANCE.getFactory()).isInstanceOf(SubwayRouteFactory.class);
        assertThat(EdgeWeightType.DURATION.getFactory()).isInstanceOf(SubwayRouteFactory.class);
    }
}