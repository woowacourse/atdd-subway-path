package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.LineStations;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.graph.PathFactory;
import wooteco.subway.admin.domain.graph.SubwayEdge;
import wooteco.subway.admin.domain.graph.SubwayShortestPath;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public PathResponse findPath(Long sourceId, Long targetId, WeightType type) {
		validateStationNamesAreSame(sourceId, targetId);

		List<Line> lines = lineRepository.findAll();
		Map<Long, Station> allStationsById = stationRepository.findAll().stream()
			.collect(Collectors.toMap(Station::getId, station -> station));

		LineStations lineStations = Line.toLineStations(lines);
		Graph<Long, SubwayEdge> subwayGraph = PathFactory.from(lineStations.get(), type);
		SubwayShortestPath subwayShortestPath = SubwayShortestPath.of(subwayGraph, sourceId, targetId);

		int totalDuration = subwayShortestPath.calculateTotalDuration();
		int totalDistance = subwayShortestPath.calculateTotalDistance();
		List<Long> shortestPathIds = subwayShortestPath.getShortestPath();
		List<Station> shortestPath = makeStationsById(allStationsById, shortestPathIds);

		return PathResponse.of(totalDistance, totalDuration, StationResponse.listOf(shortestPath));
	}

	private void validateStationNamesAreSame(Long source, Long target) {
		if (source.equals(target)) {
			throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
		}
	}

	private List<Station> makeStationsById(Map<Long, Station> allStationsById, List<Long> shortestPathIds) {
		return shortestPathIds.stream()
			.map(allStationsById::get)
			.collect(Collectors.toList());
	}
}
