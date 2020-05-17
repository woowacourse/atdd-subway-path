package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayShortestPath {
	private final GraphPath<Station, Edge> graphPath;

	private SubwayShortestPath(final GraphPath<Station, Edge> graphPath) {
		this.graphPath = graphPath;
	}

	public static SubwayShortestPath of(final List<Line> lines,
		final List<Station> stations,
		final Station source, final Station target, final PathType type) {
		WeightedMultigraph<Station, Edge> graph
			= new WeightedMultigraph<>(Edge.class);

		addStationsInGraph(stations, graph);
		addLineInGraph(lines, stations, type, graph);

		DijkstraShortestPath<Station, Edge> dijkstraShortestPath
			= new DijkstraShortestPath<>(graph);

		return new SubwayShortestPath(dijkstraShortestPath.getPath(source, target));
	}

	private static void addStationsInGraph(List<Station> stations,
		WeightedMultigraph<Station, Edge> graph) {
		for (Station station : stations) {
			graph.addVertex(station);
		}
	}

	private static void addLineInGraph(List<Line> lines, List<Station> stations,
		PathType type,
		WeightedMultigraph<Station, Edge> graph) {
		for (Line line : lines) {
			addEdgeOfLine(line, stations, type, graph);
		}
	}

	private static void addEdgeOfLine(Line line, List<Station> stations, PathType type,
		WeightedMultigraph<Station, Edge> graph) {
		for (LineStation lineStation : line.getStations()) {
			addEdge(stations, type, graph, lineStation);
		}
	}

	private static void addEdge(List<Station> stations, PathType type,
		WeightedMultigraph<Station, Edge> graph, LineStation lineStation) {
		if (isNotNull(lineStation)) {
			Edge edge = Edge.of(lineStation);
			Station preStation = findStation(stations, lineStation.getPreStationId());
			Station station = findStation(stations, lineStation.getStationId());
			graph.addEdge(preStation, station, edge);
			graph.setEdgeWeight(edge, type.findWeightOf(lineStation));
		}
	}

	private static boolean isNotNull(LineStation lineStation) {
		return Objects.nonNull(lineStation.getPreStationId());
	}

	private static Station findStation(List<Station> stations, Long id) {
		return stations.stream()
			.filter(station -> station.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다." + id));
	}

	public List<Edge> getEdgeList() {
		return graphPath.getEdgeList();
	}

	public List<Station> getVertexList() {
		return graphPath.getVertexList();
	}

	public int getDistance() {
		return graphPath.getEdgeList().stream()
			.mapToInt(Edge::getDistance)
			.sum();
	}

	public int getDuration() {
		return getEdgeList().stream()
			.mapToInt(Edge::getDuration)
			.sum();
	}

	public GraphPath<Station, Edge> getGraphPath() {
		return graphPath;
	}
}
