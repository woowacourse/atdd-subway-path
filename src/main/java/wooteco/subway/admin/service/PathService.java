package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public ShortestPathResponse getShortestPath(String source, String target) {
		Station sourceStation = stationRepository.findByName(source).orElseThrow(IllegalArgumentException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(IllegalArgumentException::new);

		Long targetStationId = targetStation.getId();
		Long sourceStationId = sourceStation.getId();
		Lines graphLines = new Lines(lineRepository.findAll());

		List<Long> shortestPath = graphLines.findShortestPath(sourceStationId, targetStationId);
		List<StationResponse> stationResponses = StationResponse.listOf(findStationsByIds(shortestPath));
		int shortestDistance = graphLines.calculateDistance(sourceStationId, targetStationId);
		int duration = graphLines.calculateDuration(sourceStationId, targetStationId);

		return new ShortestPathResponse(stationResponses, shortestDistance, duration);
	}

	private List<Station> findStationsByIds(List<Long> shortestPath) {
		return shortestPath.stream()
			.map(stationId -> stationRepository.findById(stationId).orElseThrow(IllegalAccessError::new))
			.collect(toList());
	}
}
