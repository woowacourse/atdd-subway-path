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

	public Path(Lines lines, Stations stations, Criteria criteria) {
		this.lines = lines;
		this.stations = stations;
		initGraph(criteria);
	}

	private void initGraph(Criteria criteria) {
		Graphs.addAllVertices(graph, stations.getStations());
		addEdges(criteria);
	}

	private void addEdges(Criteria criteria) {
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
	}

	public ShortestPath findShortestPath(Station sourceStation, Station targetStation) {
		GraphPath<Station, Edge> result = getDijkstraShortestPath(graph, sourceStation, targetStation);
		return new ShortestPath(result.getVertexList(), getTotalDistance(result), getTotalDuration(result));
	}

	private int getTotalDuration(GraphPath<Station, Edge> result) {
		return result.getEdgeList().stream()
			.mapToInt(Edge::getDuration)
			.sum();
	}

	private int getTotalDistance(GraphPath<Station, Edge> result) {
		return result.getEdgeList().stream()
			.mapToInt(Edge::getDistance)
			.sum();
	}

	private GraphPath<Station, Edge> getDijkstraShortestPath(WeightedMultigraph<Station, Edge> graph,
		Station sourceStation, Station targetStation) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}
}
