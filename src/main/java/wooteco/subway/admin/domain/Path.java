package wooteco.subway.admin.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
	private final WeightedMultigraph<Station, Edge> graph;

	public Path(WeightedMultigraph<Station, Edge> graph, Lines lines, Stations stations, Criteria criteria) {
		this.graph = graph;
		initGraph(stations, lines, criteria);
	}

	private void initGraph(Stations stations, Lines lines, Criteria criteria) {
		Graphs.addAllVertices(graph, stations.getStations());
		for (LineStation lineStation : lines.toLineStations()) {
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

	public GraphPath<Station, Edge> findShortestPath(Station sourceStation, Station targetStation) {
		return getDijkstraShortestPath(sourceStation, targetStation);
	}


	private GraphPath<Station, Edge> getDijkstraShortestPath(Station sourceStation, Station targetStation) {
		DijkstraShortestPath<Station, Edge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

		return dijkstraShortestPath.getPath(sourceStation, targetStation);
	}


}
