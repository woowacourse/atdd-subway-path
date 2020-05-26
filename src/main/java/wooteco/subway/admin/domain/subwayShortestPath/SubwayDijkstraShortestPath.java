package wooteco.subway.admin.domain.subwayShortestPath;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.exceptions.IllegalPathException;

public class SubwayDijkstraShortestPath implements SubwayShortestPath {
	private final GraphPath<Station, Edge> graphPath;

	private SubwayDijkstraShortestPath(final GraphPath<Station, Edge> graphPath) {
		this.graphPath = graphPath;
	}

	public static SubwayShortestPath of(final List<Line> lines,
		final List<Station> stations, final Station source, final Station target,
		final PathType type) {
		validateSourceAndTarget(source, target);

		WeightedMultigraph<Station, Edge> graph
			= new WeightedMultigraph<>(Edge.class);

		addStationsInGraph(stations, graph);
		addLineInGraph(lines, stations, type, graph);

		GraphPath<Station, Edge> graphPath = createGraphPath(source, target,
			graph);
		validatePath(graphPath);

		return new SubwayDijkstraShortestPath(graphPath);
	}

	private static void validateSourceAndTarget(Station source, Station target) {
		if (source.equals(target)) {
			throw new IllegalPathException("출발역과 도착역은 같을 수 없습니다.");
		}
	}

	private static GraphPath<Station, Edge> createGraphPath(Station source,
		Station target, WeightedMultigraph<Station, Edge> graph) {
		try {
			DijkstraShortestPath<Station, Edge> dijkstraShortestPath
				= new DijkstraShortestPath<>(graph);
			return dijkstraShortestPath.getPath(source, target);
		} catch (IllegalArgumentException e) {
			throw new IllegalPathException(e.getMessage());
		}
	}

	private static void validatePath(GraphPath<Station, Edge> graphPath) {
		if (graphPath == null) {
			throw new IllegalPathException("가능한 경로가 없습니다.");
		}
	}

	private static void addStationsInGraph(List<Station> stations,
		WeightedMultigraph<Station, Edge> graph) {
		for (Station station : stations) {
			graph.addVertex(station);
		}
	}

	private static void addLineInGraph(List<Line> lines, List<Station> stations,
		PathType type, WeightedMultigraph<Station, Edge> graph) {
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

	@Override
	public List<Station> getVertexList() {
		return graphPath.getVertexList();
	}

	@Override
	public Weight getWeight() {
		Weight weight = Weight.zero();
		for (Edge edge : graphPath.getEdgeList()) {
			weight = weight.addWeight(edge.getDistance(), edge.getDuration());
		}
		return weight;
	}
}
