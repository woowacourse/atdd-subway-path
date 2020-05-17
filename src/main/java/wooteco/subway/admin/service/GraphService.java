package wooteco.subway.admin.service;

import java.util.List;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Graph;
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
		if (source.equals(target)) {
			throw new IllegalArgumentException("출발지와 목적지가 동일합니다!");
		}
		List<Line> lines = lineRepository.findAll();
		List<Station> stations = stationRepository.findAll();

		Station sourceStation = stationRepository.findByName(source).orElseThrow(RuntimeException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(RuntimeException::new);

		Graph graph = Graph.of(lines, stations, pathType);
		GraphPath<Station, Edge> path = graph.findPath(sourceStation, targetStation);
		int distance = path.getEdgeList().stream()
			.mapToInt(Edge::getDistance)
			.sum();
		int duration = path.getEdgeList().stream()
			.mapToInt(Edge::getDuration)
			.sum();

		return PathResponse.of(path.getVertexList(), distance, duration);
	}
}