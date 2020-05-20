package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;

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

		Station sourceStation = stationRepository.findByName(source)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));
		Station targetStation = stationRepository.findByName(target)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도착역입니다."));

		Graph graph = Graph.of(lines, stations, pathType);
		GraphPath<Station, Edge> path = graph.findPath(sourceStation, targetStation);
		if (Objects.isNull(path)) {
			throw new IllegalArgumentException("출발지와 목적지가 연결되어있지 않습니다.");
		}

		int distance = 0;
		int duration = 0;
		for (Edge edge : path.getEdgeList()) {
			distance += edge.getDistance();
			duration += edge.getDuration();
		}

		return PathResponse.of(path.getVertexList(), distance, duration);
	}
}