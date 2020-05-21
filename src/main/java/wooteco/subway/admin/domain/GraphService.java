package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
	public List<PathResponse> findAllPath(Stations stations, Lines lines, Long departStationId, Long arrivalStationId) {
		return Arrays.stream(EdgeWeightType.values())
			.map(type -> Optional.of(getGraphPath(stations.getStations(), lines.getLines(), type))
				.map(graph -> graph.getPath(departStationId, arrivalStationId))
				.orElseThrow(() -> new InaccessibleStationException("갈 수 없는 역입니다.")))
			.map(path -> PathResponse.of(mapToStationResponse(path.getVertexList(), stations), sumDistance(path),
				sumDuration(path)))
			.collect(Collectors.toList());
	}

	private DijkstraShortestPath<Long, CustomEdge> getGraphPath(List<Station> stations,
		List<Line> lines, EdgeWeightType type) {
		WeightedMultigraph<Long, CustomEdge> graph = new WeightedMultigraph<>(CustomEdge.class);
		stations.forEach(station -> graph.addVertex(station.getId()));
		lines.forEach(line -> line.setGraph(graph, type));

		return new DijkstraShortestPath<>(graph);
	}

	private int sumDistance(GraphPath<Long, CustomEdge> path) {
		return path.getEdgeList()
			.stream()
			.mapToInt(CustomEdge::getDistance)
			.sum();
	}

	private int sumDuration(GraphPath<Long, CustomEdge> path) {
		return path.getEdgeList()
			.stream()
			.mapToInt(CustomEdge::getDuration)
			.sum();
	}

	private List<StationResponse> mapToStationResponse(List<Long> shortestPath, Stations stations) {
		return shortestPath.stream()
			.map(stations::findById)
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}
}
