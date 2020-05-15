package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse searchPath(String source, String target, Boolean isDistance) {
		if (isDuplicatedStations(source, target)) {
			throw new DuplicatedStationNamesException();
		}

		if (stationRepository.existsByName(source) && stationRepository.existsByName(target)) {
			List<Long> allStationsIds = stationRepository.findAllIds();
			List<LineStation> allLineStations = lineRepository.findAllLineStations();
			Station sourceStation = stationRepository.findByName(source).orElseThrow(NoSuchElementException::new);
			Station targetStation = stationRepository.findByName(target).orElseThrow(NoSuchElementException::new);

			DijkstraShortestPath dijkstraShortestPath = findDijkstraShortestPath(
					allStationsIds, allLineStations, isDistance);

			List<String> shortestPath = dijkstraShortestPath.getPath(
					String.valueOf(sourceStation.getId()), String.valueOf(targetStation.getId())).getVertexList();

			if (shortestPath.contains("")) {
				throw new UnconnectedStationsException();
			}

			List<Station> stations = shortestPath.stream()
					.map(it -> stationRepository.findById(Long.valueOf(it)))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(Collectors.toList());

			List<Long> stationIds = stations.stream()
					.map(Station::getId)
					.collect(Collectors.toList());

			int totalDistance = calculateTotalDistance(stationIds);
			int totalDuration = calculateTotalDuration(stationIds);

			return new PathResponse(stations, totalDistance, totalDuration);
		}
		throw new NotExistStationException();
	}

	private boolean isDuplicatedStations(String source, String target) {
		return Objects.equals(source, target);
	}

	private int calculateTotalDistance(List<Long> stations) {
		return IntStream.range(0, stations.size() - 1)
				.mapToObj(it -> lineRepository.findLineStationByPreStationIdAndStationId(stations.get(it),
				                                                                         stations.get(it + 1)))
				.mapToInt(LineStation::getDistance)
				.sum();
	}

	private int calculateTotalDuration(List<Long> stations) {
		return IntStream.range(0, stations.size() - 1)
				.mapToObj(it -> lineRepository.findLineStationByPreStationIdAndStationId(stations.get(it),
				                                                                         stations.get(it + 1)))
				.mapToInt(LineStation::getDuration)
				.sum();
	}

	private DijkstraShortestPath findDijkstraShortestPath(
			List<Long> allStationsIds, List<LineStation> allLineStations, Boolean isDistance) {

		WeightedMultigraph<String, DefaultWeightedEdge> graph
				= new WeightedMultigraph(DefaultWeightedEdge.class);

		graph.addVertex("");
		allStationsIds.forEach(id -> graph.addVertex(String.valueOf(id)));

		allLineStations.forEach(it -> {
			String preStationIdValue = createStringValue(it.getPreStationId());
			String stationIdValue = String.valueOf(it.getStationId());
			graph.setEdgeWeight(graph.addEdge(preStationIdValue, stationIdValue),
			                    isDistance ? it.getDistance() : it.getDuration());
		});


		return new DijkstraShortestPath(graph);
	}

	private String createStringValue(Long preStationId) {
		if (Objects.isNull(preStationId)) {
			return "";
		}
		return String.valueOf(preStationId);
	}
}
