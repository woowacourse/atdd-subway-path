package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.SearchType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationNamesException;
import wooteco.subway.admin.exceptions.NotExistStationException;
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
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse searchPath(final String source, final String target, final SearchType searchType) {
		if (Objects.equals(source, target)) {
			throw new DuplicatedStationNamesException();
		}

		if (stationRepository.notExistsByName(source) | stationRepository.notExistsByName((target))) {
			throw new NotExistStationException();
		}

		List<Station> allStations = stationRepository.findAll();
		List<LineStation> allLineStations = lineRepository.findAllLineStations();

		List<String> stationIdsOfShortestPath = findShortestPath(source, target, searchType, allStations, allLineStations);

		if (stationIdsOfShortestPath.contains("")) {
			throw new UnconnectedStationsException();
		}

		List<Station> stationsOnPath = stationIdsOfShortestPath.stream()
				.map(it -> findStation(allStations, Long.valueOf(it)))
				.collect(Collectors.toList());

		int totalDistance = calculateTotalDistance(stationsOnPath);
		int totalDuration = calculateTotalDuration(stationsOnPath);

		return new PathResponse(stationsOnPath, totalDistance, totalDuration);
	}

	private Station findStation(final List<Station> allStations, final Long stationId) {
		return allStations.stream()
				.filter(it -> Objects.equals(it.getId(), stationId))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);
	}

	private List<String> findShortestPath(final String source, final String target, final SearchType searchType,
										  final List<Station> allStations, final List<LineStation> allLineStations) {
		Station sourceStation = stationRepository.findByName(source).orElseThrow(NoSuchElementException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(NoSuchElementException::new);

		DijkstraShortestPath dijkstraShortestPath = findDijkstraPath(
				allStations, allLineStations, searchType);

		return dijkstraShortestPath.getPath(
				String.valueOf(sourceStation.getId()), String.valueOf(targetStation.getId())).getVertexList();
	}

	private DijkstraShortestPath findDijkstraPath(final List<Station> allStations,
												  final List<LineStation> allLineStations,
												  final SearchType searchType) {

		WeightedMultigraph<String, DefaultWeightedEdge> graph
				= new WeightedMultigraph(DefaultWeightedEdge.class);

		graph.addVertex("");
		allStations.forEach(station -> graph.addVertex(String.valueOf(station.getId())));
		allLineStations.forEach(it -> {
			String preStationIdValue = createStringValueOf(it.getPreStationId());
			String stationIdValue = String.valueOf(it.getStationId());
			graph.setEdgeWeight(graph.addEdge(preStationIdValue, stationIdValue),
			                    searchType.isDistance() ? it.getDistance() : it.getDuration());
		});

		return new DijkstraShortestPath(graph);
	}

	private String createStringValueOf(final Long preStationId) {
		if (Objects.isNull(preStationId)) {
			return "";
		}
		return String.valueOf(preStationId);
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
