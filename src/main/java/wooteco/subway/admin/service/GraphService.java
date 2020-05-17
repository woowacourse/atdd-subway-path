package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

/**
 *    GraphService 클래스입니다.
 *
 *    @author HyungJu An, KuenHwi Choi
 */
@Service
public class GraphService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public GraphService(final LineRepository lineRepository, final StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse searchPath(String source, String target, PathType pathType) {
		List<Line> lines = lineRepository.findAll();
		List<Station> stations = stationRepository.findAll();

		Station sourceStation = stationRepository.findByName(source).orElseThrow(RuntimeException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(RuntimeException::new);

		GraphPath<Station, Edge> path = findPath(lines, stations, sourceStation, targetStation, pathType);
		int distance = path.getEdgeList().stream()
			.mapToInt(Edge::getDistance)
			.sum();
		int duration = path.getEdgeList().stream()
			.mapToInt(Edge::getDuration)
			.sum();

		return PathResponse.of(path.getVertexList(), distance, duration);
	}

	private GraphPath<Station, Edge> findPath(List<Line> lines, List<Station> stations,
		Station source, Station target, PathType type) {
		WeightedMultigraph<Station, Edge> graph
			= new WeightedMultigraph<>(Edge.class);

		stations.forEach(graph::addVertex);

		lines.stream()
			.flatMap(it -> it.getStations().stream())
			.filter(it -> Objects.nonNull(it.getPreStationId()))
			.forEach(it -> {
				Edge edge = Edge.of(it);
				graph.addEdge(findStation(stations, it.getPreStationId()),
					findStation(stations, it.getStationId()), edge);
				graph.setEdgeWeight(edge, type.findWeightOf(it));
			});

		return DijkstraShortestPath.findPathBetween(graph, source, target);
	}

	private Station findStation(List<Station> stations, Long id) {
		return stations.stream()
			.filter(station -> station.getId().equals(id))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(id + "는 존재하지 않는 역입니다."));
	}
}