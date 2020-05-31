package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.InaccessibleStationException;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
public class GraphService {
	public PathResponse findPath(Stations stations, Lines lines, Long departStationId, Long arrivalStationId,
		EdgeWeightType type) {
		GraphPath<Long, CustomEdge> graphPath = getGraphPath(stations, lines, departStationId, arrivalStationId, type);
		List<Long> stationIds = graphPath.getVertexList();

		return PathResponse.of(mapToStationResponse(stationIds, stations), sumDistance(graphPath),
			sumDuration(graphPath));
	}

	private GraphPath<Long, CustomEdge> getGraphPath(Stations stations, Lines lines, Long departStationId,
		Long arrivalStationId, EdgeWeightType type) {
		GraphPath<Long, CustomEdge> path = getDijkstraPath(stations.getStations(), lines.getLines(), type)
			.getPath(departStationId, arrivalStationId);

		if (Objects.isNull(path)) {
			throw new InaccessibleStationException("갈 수 없는 역입니다.");
		}
		return path;
	}

	private DijkstraShortestPath<Long, CustomEdge> getDijkstraPath(List<Station> stations,
		List<Line> lines, EdgeWeightType type) {
		WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);
		stations.forEach(station -> graph.addVertex(station.getId()));
		lines.forEach(line -> line.setUpGraph(graph, type));

		return new DijkstraShortestPath<>(graph);
	}

	private int sumDistance(GraphPath<Long, CustomEdge> path) {
		return (int)path.getEdgeList()
			.stream()
			.mapToDouble(CustomEdge::getDistance)
			.sum();
	}

	private int sumDuration(GraphPath<Long, CustomEdge> path) {
		return (int)path.getEdgeList()
			.stream()
			.mapToDouble(CustomEdge::getDuration)
			.sum();
	}

	private List<StationResponse> mapToStationResponse(List<Long> shortestPath, Stations stations) {
		return shortestPath.stream()
			.map(stations::findById)
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}
}
