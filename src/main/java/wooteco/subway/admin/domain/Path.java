package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.dto.ShortestPath;

public class Path {
	private final WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph(Edge.class);
	private final Lines lines;
	private final Stations stations;

	public Path(Lines lines, Stations stations) {
		this.lines = lines;
		this.stations = stations;
	}

	public ShortestPath findShortestPath(Station sourceStation, Station targetStation, Criteria criteria) {
		// TODO: 2020-05-17 메서드 분리 등 리팩토링 필요
		Graphs.addAllVertices(graph, stations.getStations());

		for (LineStation lineStation : lines.toLineStations()) {
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

		int totalDistance = result.getEdgeList().stream()
			.mapToInt(Edge::getDistance)
			.sum();

		int totalDuration = result.getEdgeList().stream()
			.mapToInt(Edge::getDuration)
			.sum();

		return new ShortestPath(result.getVertexList(), totalDistance, totalDuration);

	}

	private GraphPath<Station, Edge> getDijkstraShortestPath(WeightedMultigraph<Station, Edge> graph,
		Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}
}
