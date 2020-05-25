package wooteco.subway.admin.domain.graph;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.type.WeightType;

public class PathFactory {
	public static Graph<Long, SubwayEdge> from(List<LineStation> allLineStations, List<Station> stations,
		WeightType weightType) {
		WeightedMultigraph<Long, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);

		for (Station station : stations) {
			graph.addVertex(station.getId());
		}

		for (LineStation lineStation : allLineStations) {
			SubwayEdge subwayEdge = new SubwayEdge(lineStation, weightType.getWeightStrategy());
			graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), subwayEdge);
		}

		return graph;
	}
}
