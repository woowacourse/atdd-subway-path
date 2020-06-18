package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Criteria;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.ShortestPath;
import wooteco.subway.admin.exception.EmptyStationNameException;
import wooteco.subway.admin.exception.NoStationNameExistsException;
import wooteco.subway.admin.exception.SourceEqualsTargetException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public ShortestPath findShortestDistancePath(String sourceName, String targetName, Criteria criteria) {
		if (sourceName.equals(targetName)) {
			throw new SourceEqualsTargetException();
		}

		if (sourceName.isEmpty() || targetName.isEmpty()) {
			throw new EmptyStationNameException();
		}

		Station sourceStation = stationRepository.findByName(sourceName)
			.orElseThrow(NoStationNameExistsException::new);
		Station targetStation = stationRepository.findByName(targetName)
			.orElseThrow(NoStationNameExistsException::new);

		Lines lines = new Lines(lineRepository.findAll());
		List<Long> lineStationIds = lines.toLineStationIds();

		Stations stations = new Stations(stationRepository.findAllById(lineStationIds));

		Path path = new Path(lines, stations, criteria);
		return path.findShortestPath(sourceStation, targetStation);
	}
}
