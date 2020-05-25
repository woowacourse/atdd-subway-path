package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.DijkstraEdgeWeightType;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStationKey;
import wooteco.subway.admin.domain.path.PathEdge;
import wooteco.subway.admin.domain.path.PathResult;
import wooteco.subway.admin.domain.path.PathWeightEdge;
import wooteco.subway.admin.domain.path.ShortestPathFinder;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exceptions.DuplicatedStationsException;
import wooteco.subway.admin.exceptions.NotExistStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public PathResponse createPathResponse(Long sourceStationId, Long targetStationId,
	                                       DijkstraEdgeWeightType edgeWeightType) {
		validateStations(sourceStationId, targetStationId);

		List<LineStation> allLineStations = lineRepository.findAllLineStations();

		ShortestPathFinder shortestPathFinder = createShortestPathFinder(edgeWeightType, allLineStations);
		PathResult pathResult = shortestPathFinder.findShortestPath(sourceStationId, targetStationId);

		List<Long> shortestPathStationIds = pathResult.getVertexes();
		List<LineStation> lineStations = findShortestPathLineStations(allLineStations, pathResult);

		return createPathResponse(shortestPathStationIds, lineStations);
	}

	private void validateStations(Long sourceStationId, Long targetStationId) {
		if (Objects.equals(sourceStationId, targetStationId)) {
			throw new DuplicatedStationsException();
		}

		stationRepository.findById(sourceStationId).orElseThrow(() -> new NotExistStationException(sourceStationId));
		stationRepository.findById(targetStationId).orElseThrow(() -> new NotExistStationException(targetStationId));
	}

	private PathResponse createPathResponse(List<Long> shortestPathStationIds, List<LineStation> lineStations) {
		List<String> shortestPathNames = stationRepository.findNamesByIds(shortestPathStationIds);

		int totalDistance = lineStations.stream()
				.mapToInt(LineStation::getDistance)
				.sum();

		int totalDuration = lineStations.stream()
				.mapToInt(LineStation::getDuration)
				.sum();

		return new PathResponse(shortestPathNames, totalDistance, totalDuration);
	}

	private List<LineStation> findShortestPathLineStations(List<LineStation> allLineStations, PathResult pathResult) {
		Map<LineStationKey, LineStation> lineStationFinder = allLineStations.stream()
				.collect(Collectors.toMap(LineStation::getLineStationKey, Function.identity()));

		return pathResult.getEdges().stream()
				.map(id -> new LineStationKey(id.getDepartureVertex(), id.getDestinationVertex()))
				.map(lineStationFinder::get)
				.collect(Collectors.toList());
	}

	private ShortestPathFinder createShortestPathFinder(DijkstraEdgeWeightType edgeWeightType,
	                                                    List<LineStation> allLineStations) {
		List<PathWeightEdge> pathWeightEdges = allLineStations.stream()
				.filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
				.map(lineStation -> new PathWeightEdge(new PathEdge(lineStation.getPreStationId(),
				                                                    lineStation.getStationId()),
				                                       edgeWeightType.getEdgeWeight(lineStation)))
				.collect(Collectors.toList());

		return new ShortestPathFinder(pathWeightEdges);
	}
}
