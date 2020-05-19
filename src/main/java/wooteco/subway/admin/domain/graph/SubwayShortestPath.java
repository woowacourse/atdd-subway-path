package wooteco.subway.admin.domain.graph;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.domain.type.weightstrategy.WeightStrategy;

public class SubwayShortestPath {

	private final GraphPath shortestPath;
	private final Map<Long, Station> allStationsById;

	public SubwayShortestPath(List<LineStation> allLineStations,
			Map<Long, Station> allStationsById,
			Station source, Station target, WeightType weightType) {

		Graph<Long, SubwayEdge> graph = createMultiGraph(
			allLineStations, allStationsById, weightType.getWeightStrategy());

		this.shortestPath = findShortestPath(source, target, graph);
		this.allStationsById = allStationsById;
	}

	private GraphPath findShortestPath(Station source, Station target, Graph<Long, SubwayEdge> graph) {
		GraphPath shortestPath = DijkstraShortestPath.findPathBetween(
			graph, source.getId(), target.getId());

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
        return shortestPathIds.stream()
                .map(allStationsById::get)
                .collect(Collectors.toList());
	}
}
