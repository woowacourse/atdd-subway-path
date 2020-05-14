package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.ShortestPath;
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
	public ShortestPath findShortestDistancePath(String sourceName, String targetName, String criteriaType) {
		Station sourceStation = stationRepository.findByName(sourceName)
				.orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 없습니다."));
		Station targetStation = stationRepository.findByName(targetName)
				.orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 없습니다."));

		Criteria criteria = Criteria.of(criteriaType);

		Subway subway = new Subway(lineRepository.findAll());
		List<Long> lineStationIds = subway.fetchLineStationIds();

		Stations stations = new Stations(stationRepository.findAllById(lineStationIds));

		Path path = new Path(subway, stations);
		return path.findShortestPath(sourceStation, targetStation, criteria);
	}
}
