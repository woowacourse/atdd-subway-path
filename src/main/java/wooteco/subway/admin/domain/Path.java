package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.dto.response.ShortestPathResponse;

public class Path {
	private final WeightedMultigraph<Station, Edge> graph;

	public Path(WeightedMultigraph<Station, Edge> graph, Lines lines, Stations stations, Criteria criteria) {
		this.graph = graph;
		initGraph(stations, lines, criteria);
	}

	private void initGraph(Stations stations, Lines lines, Criteria criteria) {
		Graphs.addAllVertices(graph, stations.getStations());
		for (LineStation lineStation : lines.fetchLineStations()) {
			addEdgeBy(lineStation, stations, criteria);
		}
	}

	private void addEdgeBy(LineStation lineStation, Stations stations, Criteria criteria) {
		if (lineStation.getPreStationId() == null) {
			return;
		}

		Station station = stations.findByKey(lineStation.getStationId());
		Station preStation = stations.findByKey(lineStation.getPreStationId());

		Edge edge = new Edge(lineStation.getDuration(), lineStation.getDistance(), criteria);

		graph.addEdge(preStation, station, edge);
		graph.setEdgeWeight(graph.getEdge(preStation, station), edge.getWeight());
	}

	public ShortestPathResponse findShortestPath(Station sourceStation, Station targetStation) {
		GraphPath<Station, Edge> result = getDijkstraShortestPath(sourceStation, targetStation);
		int totalDistance = calculateTotalDistance(result);
		int totalDuration = calculateTotalDuration(result);

		return new ShortestPathResponse(result.getVertexList(), totalDistance, totalDuration);
	}


	private GraphPath<Station, Edge> getDijkstraShortestPath(Station sourceStation, Station targetStation) {
		DijkstraShortestPath<Station, Edge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}

	private int calculateTotalDuration(GraphPath<Station, Edge> result) {
		return result.getEdgeList().stream()
				.mapToInt(Edge::getDuration)
				.sum();
	}

	private int calculateTotalDistance(GraphPath<Station, Edge> result) {
		return result.getEdgeList().stream()
				.mapToInt(Edge::getDistance)
				.sum();
}
}
