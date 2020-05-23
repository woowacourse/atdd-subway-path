package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.DijkstraEdgeWeightType;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.ShortestPathFinder;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationsException;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public List<Station> findAllStations() {
		return stationRepository.findAll();
	}

	public PathResponse searchPath(final Long sourceStationId, final Long targetStationId,
	                               final DijkstraEdgeWeightType edgeWeightType) {
		if (Objects.equals(sourceStationId, targetStationId)) {
			throw new DuplicatedStationsException();
		}

		stationRepository.findById(sourceStationId).orElseThrow(() -> new NotExistStationException(sourceStationId));
		stationRepository.findById(targetStationId).orElseThrow(() -> new NotExistStationException(targetStationId));
		List<Station> allStations = stationRepository.findAll();
		List<LineStation> allLineStations = lineRepository.findAllLineStations();

		ShortestPathFinder shortestPathFinder = new ShortestPathFinder(allStations, allLineStations);
		List<String> shortestPathStationsNames = shortestPathFinder.findShortestPathStationsNames(sourceStationId,
		                                                                                          targetStationId,
		                                                                                          edgeWeightType);
		List<LineStation> shortestPathLineStations = shortestPathFinder.findShortestPathLineStations(sourceStationId,
		                                                                                             targetStationId,
		                                                                                             edgeWeightType);
		int totalDistance = calculateTotalDistance(shortestPathLineStations);
		int totalDuration = calculateTotalDuration(shortestPathLineStations);

		return new PathResponse(shortestPathStationsNames, totalDistance, totalDuration);
	}

	private int calculateTotalDistance(final List<LineStation> shortestPathLineStations) {
		return shortestPathLineStations.stream()
				.mapToInt(LineStation::getDistance)
				.sum();
	}

	private int calculateTotalDuration(final List<LineStation> shortestPathLineStations) {
		return shortestPathLineStations.stream()
				.mapToInt(LineStation::getDuration)
				.sum();
	}
}
