package wooteco.subway.admin.domain.graph;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.type.weightstrategy.DurationWeightStrategy;

class SubwayEdgeTest {

	@DisplayName("SubwayEdge의 타입별 Weight를 확인한다.")
	@Test
	void name() {
		LineStation lineStation = new LineStation(1L, 2L, 10, 20);
		SubwayEdge subwayEdge = new SubwayEdge(lineStation, new DurationWeightStrategy());

		assertThat(subwayEdge.getWeight()).isEqualTo(20);
	}
}