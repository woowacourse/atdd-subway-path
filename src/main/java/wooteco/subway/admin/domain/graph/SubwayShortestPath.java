package wooteco.subway.admin.domain.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;

public class SubwayShortestPath {
	private final GraphPath shortestPath;
	private final Map<Long, Station> allStationsById;

	public SubwayShortestPath(List<Line> allLines, Map<Long, Station> allStationsById, String sourceName,
		String targetName, String type) {
		WeightType weightType = WeightType.of(type);
		Station source = findStationByName(sourceName, allStationsById);
		Station target = findStationByName(targetName, allStationsById);

		List<LineStation> allLineStations = makeLineStations(allLines);
		Graph<Long, SubwayEdge> graph = createMultiGraph(allLineStations, allStationsById,
			weightType.getWeightStrategy());

		this.shortestPath = findShortestPath(source, target, graph);
		this.allStationsById = allStationsById;
	}

	private GraphPath findShortestPath(Station source, Station target, Graph<Long, SubwayEdge> graph) {
		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		GraphPath shortestPath = dijkstraShortestPath.getPath(source.getId(), target.getId());
		if (Objects.isNull(shortestPath)) {
			throw new PathNotFoundException(PathNotFoundException.PATH_NOT_FOUND_MESSAGE);
		}
		return shortestPath;
	}

	public int calculateTotalDistance() {
		List<SubwayEdge> edgeList = this.shortestPath.getEdgeList();
		return edgeList.stream()
			.mapToInt(SubwayEdge::getDistance)
			.sum();
	}

	public int calculateTotalDuration() {
		List<SubwayEdge> edgeList = this.shortestPath.getEdgeList();
		return edgeList.stream()
			.mapToInt(SubwayEdge::getDuration)
			.sum();
	}

	public List<Station> getShortestPath() {
		List<Long> shortestPathIds = shortestPath.getVertexList();
		return makeStationsById(allStationsById, shortestPathIds);
	}

	private Station findStationByName(String targetName, Map<Long, Station> stations) {
		return stations.values().stream().filter(station -> station.isSameName(targetName))
			.findAny().orElseThrow(() -> new IllegalArgumentException(String.format("%s은 존재하지 않는 역입니다.", targetName)));
	}

	private List<LineStation> makeLineStations(List<Line> lines) {
		List<LineStation> lineStations = new ArrayList<>();

		for (Line line : lines) {
			for (LineStation lineStation : line.getLineStations()) {
				if (lineStation.isStart()) {
					continue;
				}
				lineStations.add(lineStation);
			}
		}
		return lineStations;
	}

	private Graph<Long, SubwayEdge> createMultiGraph(List<LineStation> allLineStations,
		Map<Long, Station> allStationsById, WeightStrategy weightStrategy) {
		WeightedMultigraph<Long, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);

		for (Station station : allStationsById.values()) {
			graph.addVertex(station.getId());
		}

		for (LineStation lineStation : allLineStations) {
			SubwayEdge subwayEdge = new SubwayEdge(lineStation, weightStrategy);
			graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(), subwayEdge);
		}
		return graph;
	}

	private List<Station> makeStationsById(Map<Long, Station> allStationsById, List<Long> shortestPathIds) {
		List<Station> shortestPath = new ArrayList<>();

		for (Long stationId : shortestPathIds) {
			Station stationInPath = allStationsById.get(stationId);
			shortestPath.add(stationInPath);
		}
		return shortestPath;
	}
}
