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
	public PathResponse findPath(String sourceName, String targetName, String type) {
		validateStationNamesAreSame(sourceName, targetName);

		List<Line> allLines = lineRepository.findAll();
		Map<Long, Station> allStationsById = stationRepository.findAll().stream()
			.collect(Collectors.toMap(Station::getId, station -> station));

		Station source = findStationByName(sourceName, allStationsById);
		Station target = findStationByName(targetName, allStationsById);

		LineStations LineStations = Line.toLineStations(allLines);
		Graph<Long, SubwayEdge> subwayGraph = PathFactory.from(allLineStations, allStationsById, WeightType.of(type));
		SubwayShortestPath subwayShortestPath = SubwayShortestPath.of(subwayGraph, source, target);

		int totalDuration = subwayShortestPath.calculateTotalDuration();
		int totalDistance = subwayShortestPath.calculateTotalDistance();
		List<Long> shortestPathIds = subwayShortestPath.getShortestPath();
		List<Station> shortestPath = makeStationsById(allStationsById, shortestPathIds);

		return PathResponse.of(totalDistance, totalDuration, shortestPath);
	}

	private void validateStationNamesAreSame(String sourceName, String targetName) {
		if (sourceName.equals(targetName)) {
			throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
		}
	}

	private Station findStationByName(String targetName, Map<Long, Station> stations) {
		return
			stations.values()
				.stream()
				.filter(station -> station.isSameName(targetName))
				.findAny()
				.orElseThrow(() -> new IllegalArgumentException(String.format("%s은 존재하지 않는 역입니다.", targetName)));
	}

	private List<Station> makeStationsById(Map<Long, Station> allStationsById, List<Long> shortestPathIds) {
		return shortestPathIds.stream()
			.map(allStationsById::get)
			.collect(Collectors.toList());
	}
}
