package wooteco.subway.admin.service;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.request.PathSearchRequest;
import wooteco.subway.admin.exception.EmptyStationNameException;
import wooteco.subway.admin.exception.NoPathExistsException;
import wooteco.subway.admin.exception.NoStationNameExistsException;
import wooteco.subway.admin.exception.SourceEqualsTargetException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public ShortestPath findShortestDistancePath(PathSearchRequest pathSearchRequest) {
		validatePathSearchRequest(pathSearchRequest);

		Station sourceStation = findStationByName(pathSearchRequest.getSource());
		Station targetStation = findStationByName(pathSearchRequest.getTarget());
		Criteria criteria = Criteria.of(pathSearchRequest.getType());

		Lines lines = new Lines(lineRepository.findAll());
		List<Long> lineStationIds = lines.toLineStationIds();
		Stations stations = new Stations(stationRepository.findAllById(lineStationIds));

		Path path = new Path(new WeightedMultigraph<>(Edge.class), lines, stations, criteria);

		return findShortestPath(sourceStation, targetStation, path);
	}

	private void validatePathSearchRequest(PathSearchRequest pathSearchRequest) {
		if (pathSearchRequest.getSource().isEmpty() || pathSearchRequest.getTarget().isEmpty()) {
			throw new EmptyStationNameException();
		}

		if (pathSearchRequest.getSource().equals(pathSearchRequest.getTarget())) {
			throw new SourceEqualsTargetException();
		}
	}

	private Station findStationByName(String source) {
		return stationRepository.findByName(source)
				.orElseThrow(NoStationNameExistsException::new);
	}

	private ShortestPath findShortestPath(Station sourceStation, Station targetStation, Path path) {
		ShortestPath shortestPath = new ShortestPath(path.findShortestPath(sourceStation, targetStation));

		validateShortestPath(shortestPath);

		return shortestPath;
	}

	private void validateShortestPath(ShortestPath shortestPath) {
		if (shortestPath.hasInvalidPath()) {
			throw new NoPathExistsException();
		}
	}
}
