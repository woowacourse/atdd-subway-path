package wooteco.subway.admin.service;

import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.response.ShortestPathResponse;
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
	public ShortestPathResponse findShortestDistancePath(String sourceName, String targetName, String criteriaType) {
		if (sourceName.isEmpty() || targetName.isEmpty()) {
			throw new EmptyStationNameException();
		}

		Station sourceStation = stationRepository.findByName(sourceName)
				.orElseThrow(NoStationNameExistsException::new);
		Station targetStation = stationRepository.findByName(targetName)
				.orElseThrow(NoStationNameExistsException::new);

		if (sourceStation.equals(targetStation)) {
			throw new SourceEqualsTargetException();
		}

		Criteria criteria = Criteria.of(criteriaType);

		Lines lines = new Lines(lineRepository.findAll());
		List<Long> lineStationIds = lines.toLineStationIds();

		Stations stations = new Stations(stationRepository.findAllById(lineStationIds));

		Path path = new Path(new WeightedMultigraph<>(Edge.class), lines, stations, criteria);
		return path.findShortestPath(sourceStation, targetStation);
	}
}
