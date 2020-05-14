package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.dto.ShortestPath;

import java.util.List;

public class Path {
	private final WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph(Edge.class);
	private final Subway subway;
	private final Stations stations;

	public Path(Subway subway, Stations stations) {
		this.subway = subway;
		this.stations = stations;
	}

	public ShortestPath findShortestPath(Station sourceStation, Station targetStation, Criteria criteria) {
		addVerticesToGraph(stations.getStations());

		for (LineStation lineStation : subway.fetchLineStations()) {
			if (lineStation.getPreStationId() == null) {
				continue;
			}
			Station station = stations.findByKey(lineStation.getStationId());
			Station preStation = stations.findByKey(lineStation.getPreStationId());

			Edge edge = new Edge(lineStation.getDuration(), lineStation.getDistance(), criteria);

			graph.addEdge(preStation, station, edge);
			graph.setEdgeWeight(graph.getEdge(preStation, station), edge.getWeight());
		}

		GraphPath<Station, Edge> result = getDijkstraShortestPath(graph, sourceStation, targetStation);

		int totalDistance = (int) result.getEdgeList().stream()
				.mapToDouble(Edge::getDistance)
				.sum();

		int totalDuration = (int) result.getEdgeList().stream()
				.mapToDouble(Edge::getDuration)
				.sum();

		return new ShortestPath(result.getVertexList(), totalDistance, totalDuration);

	}

	private void addVerticesToGraph(List<Station> vertices) {
		Graphs.addAllVertices(graph, vertices);
	}

	private GraphPath<Station, Edge> getDijkstraShortestPath(WeightedMultigraph<Station, Edge> graph, Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}
}
