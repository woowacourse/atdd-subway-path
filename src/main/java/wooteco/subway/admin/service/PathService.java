package wooteco.subway.admin.service;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.request.PathSearchRequest;
import wooteco.subway.admin.exception.EmptyStationNameException;
import wooteco.subway.admin.exception.NoStationNameExistsException;
import wooteco.subway.admin.exception.SourceEqualsTargetException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public ShortestPath findShortestDistancePath(PathSearchRequest pathSearchRequest) {
		if (pathSearchRequest.getSource().isEmpty() || pathSearchRequest.getTarget().isEmpty()) {
			throw new EmptyStationNameException();
		}

		if (pathSearchRequest.getSource().equals(pathSearchRequest.getTarget())) {
			throw new SourceEqualsTargetException();
		}

		Station sourceStation = findStationByName(pathSearchRequest.getSource());
		Station targetStation = findStationByName(pathSearchRequest.getTarget());

		Criteria criteria = Criteria.of(pathSearchRequest.getType());

		Lines lines = new Lines(lineRepository.findAll());
		List<Long> lineStationIds = lines.toLineStationIds();

		Stations stations = new Stations(stationRepository.findAllById(lineStationIds));

		Path path = new Path(new WeightedMultigraph<>(Edge.class), lines, stations, criteria);

		return new ShortestPath(path.findShortestPath(sourceStation, targetStation));
	}

	private Station findStationByName(String source) {
		return stationRepository.findByName(source)
				.orElseThrow(NoStationNameExistsException::new);
	}
}
