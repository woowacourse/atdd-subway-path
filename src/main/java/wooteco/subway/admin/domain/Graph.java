package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

/**
 *    Graph 클래스입니다.
 *
 *    @author HyungJu An
 */
public class Graph {
	private final WeightedMultigraph<Station, Edge> graph;

	private Graph(
		final WeightedMultigraph<Station, Edge> graph) {
		this.graph = graph;
	}

	public static Graph of(final List<Line> lines, final List<Station> stations, final PathType pathType) {
		WeightedMultigraph<Station, Edge> multiGraph = new WeightedMultigraph<>(Edge.class);

		stations.forEach(multiGraph::addVertex);
		makeEdges(lines, stations, pathType, multiGraph);

		return new Graph(multiGraph);
	}

	private static void makeEdges(final List<Line> lines, final List<Station> stations, final PathType pathType,
		final WeightedMultigraph<Station, Edge> multiGraph) {
		lines.stream()
			.flatMap(it -> it.getStations().stream())
			.filter(it -> Objects.nonNull(it.getPreStationId()))
			.forEach(it -> {
				Edge edge = Edge.of(it);
				multiGraph.addEdge(findStation(stations, it.getPreStationId()),
					findStation(stations, it.getStationId()), edge);
				multiGraph.setEdgeWeight(edge, pathType.findWeightOf(it));
			});
	}

	private static Station findStation(List<Station> stations, Long id) {
		return stations.stream()
			.filter(station -> station.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(id + "는 존재하지 않는 역입니다."));
	}

	public GraphPath<Station, Edge> findPath(Station source, Station target) {
		return DijkstraShortestPath.findPathBetween(this.graph, source, target);
	}
}
