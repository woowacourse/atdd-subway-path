package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathCalculator;
import wooteco.subway.admin.domain.SearchType;
import wooteco.subway.admin.domain.Station;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class PathService {
	private static final long START_VERTEX = 0L;

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final PathCalculator pathCalculator;

	public PathService(final LineRepository lineRepository, final StationRepository stationRepository, final PathCalculator pathCalculator) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.pathCalculator = pathCalculator;
	}

	public PathResponse searchPath(final String source, final String target, final SearchType searchType) {
		if (Objects.equals(source, target)) {
			throw new DuplicatedStationNamesException();
		}

		Station sourceStation = stationRepository.findByName(source).orElseThrow(NotExistSourceStationException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(NotExistTargetStationException::new);

		List<Station> allStations = stationRepository.findAll();
		List<LineStation> allLineStations = lineRepository.findAllLineStations();

		List<Long> pathResult = pathCalculator.findShortestPath(
				sourceStation, targetStation, searchType, allStations, allLineStations);

		if (isStationsUnconnected(pathResult)) {
			throw new UnconnectedStationsException();
		}

		List<Station> stationsOnPath = pathResult.stream()
				.map(it -> findStation(allStations, it))
				.collect(Collectors.toList());

		int totalDistance = calculateTotalDistance(stationsOnPath);
		int totalDuration = calculateTotalDuration(stationsOnPath);

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

	private int calculateTotalDistance(final List<Station> stationsOnPath) {
		return findLineStations(stationsOnPath)
				.mapToInt(LineStation::getDistance)
				.sum();
	}

	private int calculateTotalDuration(final List<Station> stationsOnPath) {
		return findLineStations(stationsOnPath)
				.mapToInt(LineStation::getDuration)
				.sum();
	}

	private Stream<LineStation> findLineStations(final List<Station> stationsOnPath) {
		return IntStream.range(0, stationsOnPath.size() - 1)
				.mapToObj(it -> lineRepository.findLineStationByPreStationIdAndStationId(
						stationsOnPath.get(it).getId(),
						stationsOnPath.get(it + 1).getId()));
	}
}
