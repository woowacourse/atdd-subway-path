package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;
import wooteco.subway.admin.repository.StationRepository;

@Transactional
@Service
public class PathService {
	private final PathAlgorithmService pathAlgorithmService;
	private final StationRepository stationRepository;

	public PathService(StationRepository stationRepository,
		PathAlgorithmService pathAlgorithmService) {
		this.stationRepository = stationRepository;
		this.pathAlgorithmService = pathAlgorithmService;
	}

	@Transactional(readOnly = true)
	public ShortestPathResponse findShortestPath(PathRequest request) {
		Stations stations = new Stations(stationRepository.findAll());
		validateDifferentSourceAndDestination(stations, request);
		return pathAlgorithmService.findShortestPath(request, stations);
	}

	private void validateDifferentSourceAndDestination(Stations stations, PathRequest request) {
		Station source = stations.findByName(request.getSource());
		Station target = stations.findByName(request.getTarget());
		if (source.equals(target)) {
			throw new SameSourceAndDestinationException();
		}
	}
}
