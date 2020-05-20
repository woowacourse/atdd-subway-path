package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationNamesException;
import wooteco.subway.admin.exceptions.NotExistSourceStationException;
import wooteco.subway.admin.exceptions.NotExistTargetStationException;
import wooteco.subway.admin.exceptions.UnconnectedStationsException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {
	private static final long START_VERTEX = 0L;

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final PathCalculator dijkstraPathCalculator;

	public PathService(final LineRepository lineRepository, final StationRepository stationRepository, final PathCalculator dijkstraPathCalculator) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.dijkstraPathCalculator = dijkstraPathCalculator;
	}

	@Transactional(readOnly = true)
	public PathResponse searchPath(final String source, final String target, final SearchType searchType) {
		if (Objects.equals(source, target)) {
			throw new DuplicatedStationNamesException();
		}

		Station sourceStation = stationRepository.findByName(source).orElseThrow(NotExistSourceStationException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(NotExistTargetStationException::new);

		List<Station> allStations = stationRepository.findAll();
		List<LineStation> allLineStations = lineRepository.findAllLineStations();

		List<Long> pathResult = dijkstraPathCalculator.findShortestPath(
				sourceStation, targetStation, searchType, allStations, allLineStations);

		if (isStationsUnconnected(pathResult)) {
			throw new UnconnectedStationsException();
		}

		List<Station> stationsOnPath = pathResult.stream()
				.map(it -> findStation(allStations, it))
				.collect(Collectors.toList());

		int totalDistance = dijkstraPathCalculator.calculateTotalDistance(stationsOnPath);
		int totalDuration = dijkstraPathCalculator.calculateTotalDuration(stationsOnPath);

		return new PathResponse(stationsOnPath, totalDistance, totalDuration);
	}

	private boolean isStationsUnconnected(final List<Long> pathResult) {
		return pathResult.contains(START_VERTEX);
	}

	private Station findStation(final List<Station> allStations, final Long stationId) {
		return allStations.stream()
				.filter(it -> Objects.equals(it.getId(), stationId))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);
	}
}
