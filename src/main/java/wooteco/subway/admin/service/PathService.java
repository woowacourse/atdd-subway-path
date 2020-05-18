package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.SearchType;
import wooteco.subway.admin.domain.ShortestPathFinder;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationNamesException;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

	public PathResponse searchPath(final Long source, final Long target, final SearchType searchType) {
		if (Objects.equals(source, target)) {
			throw new DuplicatedStationNamesException();
		}

		Station sourceStation = stationRepository.findById(source).orElseThrow(NotExistStationException::new);
		Station targetStation = stationRepository.findById(target).orElseThrow(NotExistStationException::new);
		List<Station> allStations = stationRepository.findAll();
		List<LineStation> allLineStations = lineRepository.findAllLineStations();

		ShortestPathFinder shortestPathFinder = new ShortestPathFinder(allStations, allLineStations);
		List<Station> shortestPathStations = shortestPathFinder.findShortestPathStations(sourceStation, targetStation,
		                                                                                 searchType);

		List<String> shortestPathStationsNames = shortestPathStations.stream()
				.map(Station::getName)
				.collect(Collectors.toList());
		int totalDistance = calculateTotalDistance(shortestPathStations);
		int totalDuration = calculateTotalDuration(shortestPathStations);

		return new PathResponse(shortestPathStationsNames, totalDistance, totalDuration);
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
