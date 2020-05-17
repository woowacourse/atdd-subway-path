package wooteco.subway.admin.domain.graph;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;

public class PathFactory {
	public static Graph<Long, SubwayEdge> from(List<LineStation> allLineStations, Map<Long, Station> allStationsById, WeightType weightType) {
		return createMultiGraph(allLineStations, allStationsById, weightType.getWeightStrategy());
	}

	private static Graph<Long, SubwayEdge> createMultiGraph(List<LineStation> allLineStations,
		Map<Long, Station> allStationsById, WeightStrategy weightStrategy) {
		WeightedMultigraph<Long, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);

		for (Station station : allStationsById.values()) {
			graph.addVertex(station.getId());
		}

		for (LineStation lineStation : allLineStations) {
			SubwayEdge subwayEdge = new SubwayEdge(lineStation, weightStrategy);
			graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), subwayEdge);
		}
		return graph;
	}
}
