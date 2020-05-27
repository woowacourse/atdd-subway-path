package wooteco.subway.admin.domain.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.type.WeightType;

public class PathFactory {
	public static Graph<Long, SubwayEdge> from(List<LineStation> lineStations, WeightType weightType) {
		WeightedMultigraph<Long, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);

		Set<Long> set = new HashSet<>();
		lineStations.forEach(lineStation -> {
			graph.addVertex(lineStation.getPreStationId());
			graph.addVertex(lineStation.getStationId());
			set.add(lineStation.getPreStationId());
			set.add(lineStation.getStationId());
		});

		System.out.println(set);

		for (LineStation lineStation : lineStations) {
			SubwayEdge subwayEdge = new SubwayEdge(lineStation, weightType.getWeightStrategy());
			graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), subwayEdge);
		}

		return graph;
	}
}
