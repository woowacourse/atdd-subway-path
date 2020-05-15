package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.dto.ShortestPath;

import java.util.Collection;

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
			makeGraph(criteria, lineStation);
		}

		GraphPath<Station, Edge> result = getDijkstraShortestPath(graph, sourceStation, targetStation);

		// result가 null이면 경로가 존재하지 않는 경우

		int totalDistance = result.getEdgeList().stream() // TODO: 2020/05/14 메서드로 빼기 아래도 적용
				.mapToInt(Edge::getDistance)
				.sum();

		int totalDuration = result.getEdgeList().stream()
				.mapToInt(Edge::getDuration)
				.sum();

		return new ShortestPath(result.getVertexList(), totalDistance, totalDuration);

	}

	private void makeGraph(Criteria criteria, LineStation lineStation) {
		if (lineStation.getPreStationId() == null) {
			return;
		}
		Station station = stations.findByKey(lineStation.getStationId());
		Station preStation = stations.findByKey(lineStation.getPreStationId());

		Edge edge = new Edge(lineStation.getDuration(), lineStation.getDistance(), criteria);

		graph.addEdge(preStation, station, edge);
		graph.setEdgeWeight(graph.getEdge(preStation, station), edge.getWeight());
	}

	private void addVerticesToGraph(Collection<Station> vertices) { // TODO: 2020/05/14 빼기?
		Graphs.addAllVertices(graph, vertices);
	}

	private GraphPath<Station, Edge> getDijkstraShortestPath(WeightedMultigraph<Station, Edge> graph, Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}
}
