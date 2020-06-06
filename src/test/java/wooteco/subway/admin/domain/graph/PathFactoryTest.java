package wooteco.subway.admin.domain.graph;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.type.WeightType;

class PathFactoryTest {

	@DisplayName("PathFactory의 그래프 생성을 확인한다.")
	@Test
	void name() {
		LineStation lineStation = new LineStation(1L, 2L, 10, 20);
		List<LineStation> lineStations = Arrays.asList(lineStation);

		Graph<Long, SubwayEdge> graph = PathFactory.from(lineStations, WeightType.DISTANCE);

		assertThat(graph.containsVertex(1L)).isTrue();
		assertThat(graph.containsVertex(2L)).isTrue();
	}
}