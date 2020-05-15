package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.graph.SubwayShortestPath;
import wooteco.subway.admin.dto.PathResponse;
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

	public PathResponse findPath(String sourceName, String targetName, String type) {
		validateStationNamesAreSame(sourceName, targetName);

		List<Line> allLines = lineRepository.findAll();
		Map<Long, Station> allStationsById = stationRepository.findAll().stream()
			.collect(Collectors.toMap(Station::getId, station -> station));

		SubwayShortestPath subwayShortestPath = new SubwayShortestPath(allLines, allStationsById, sourceName,
			targetName, type);

		int totalDuration = subwayShortestPath.calculateTotalDuration();
		int totalDistance = subwayShortestPath.calculateTotalDistance();
		List<Station> shortestPath = subwayShortestPath.getShortestPath();

		return PathResponse.of(totalDistance, totalDuration, shortestPath);
	}

	private void validateStationNamesAreSame(String sourceName, String targetName) {
		if (sourceName.equals(targetName)) {
			throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
		}
	}
}
