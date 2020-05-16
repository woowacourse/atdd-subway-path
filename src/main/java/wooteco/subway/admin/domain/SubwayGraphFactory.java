package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Map;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraphFactory {

	public static WeightedMultigraph<Station, SubwayWeightedEdge> createDistanceGraph(
		List<LineStation> lineStations, Map<Long, Station> stations) {

		WeightedMultigraph<Station, SubwayWeightedEdge> graph =
			new WeightedMultigraph<>(SubwayWeightedEdge.class);

		addVertexes(graph, stations);
		for (LineStation lineStation : lineStations) {
			setEdgeWeightByDistance(stations, lineStation, graph);
		}
		return graph;
	}

	public static WeightedMultigraph<Station, SubwayWeightedEdge> createDurationGraph(
		List<LineStation> lineStations, Map<Long, Station> stations) {

		WeightedMultigraph<Station, SubwayWeightedEdge> graph =
			new WeightedMultigraph<>(SubwayWeightedEdge.class);

		addVertexes(graph, stations);
		for (LineStation lineStation : lineStations) {
			setEdgeWeightByDuration(stations, lineStation, graph);
		}
		return graph;
	}

	private static void addVertexes(
		WeightedMultigraph<Station, SubwayWeightedEdge> graph,
		Map<Long, Station> stations) {
		for (Station station : stations.values()) {
			graph.addVertex(station);
		}
	}


	private static void setEdgeWeightByDistance(Map<Long, Station> stations,
		LineStation lineStation, WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
		if (lineStation.getPreStationId() == null) {
			return;
		}

		SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(
			stations.get(lineStation.getPreStationId()),
			stations.get(lineStation.getStationId()));
		subwayWeightedEdge.setSubWeight(lineStation.getDuration());

		graph.setEdgeWeight(subwayWeightedEdge, lineStation.getDistance());
	}

	private static void setEdgeWeightByDuration(Map<Long, Station> stations,
		LineStation lineStation, WeightedMultigraph<Station, SubwayWeightedEdge> graph) {
		if (lineStation.getPreStationId() == null) {
			return;
		}

		SubwayWeightedEdge subwayWeightedEdge = graph.addEdge(
			stations.get(lineStation.getPreStationId()),
			stations.get(lineStation.getStationId()));
		subwayWeightedEdge.setSubWeight(lineStation.getDistance());

		graph.setEdgeWeight(subwayWeightedEdge, lineStation.getDuration());
	}
}
