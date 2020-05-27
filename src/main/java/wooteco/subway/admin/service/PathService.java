package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
import wooteco.subway.admin.domain.graph.SubwayShortestPath;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
	public static final String ERROR_MESSAGE_START_IS_SAME_TO_END = "출발역과 도착역이 같습니다.";
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse findPath(String sourceName, String targetName, WeightType type) {
		validateStationNamesAreSame(sourceName, targetName);

		List<LineStation> allLineStations = Line.findAllLineStationsWithoutStart(lineRepository.findAll());
		Map<Long, Station> allStationsById = stationRepository.findAll().stream()
			.collect(Collectors.toMap(Station::getId, station -> station));

		Station source = Station.findStationByName(sourceName, allStationsById);
		Station target = Station.findStationByName(targetName, allStationsById);

		SubwayShortestPath subwayShortestPath = new SubwayShortestPath(
			allLineStations, allStationsById, source, target, type);

		return PathResponse.from(subwayShortestPath);
	}

	private void validateStationNamesAreSame(String sourceName, String targetName) {
		if (sourceName.equals(targetName)) {
			throw new IllegalArgumentException(ERROR_MESSAGE_START_IS_SAME_TO_END);
		}
	}
}
