package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathGenerator;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public WholeSubwayResponse wholeLines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(line -> {
				List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
				return LineDetailResponse.of(line, stations);
			})
			.collect(Collectors.collectingAndThen(Collectors.toList(), WholeSubwayResponse::of));
	}

	@Transactional(readOnly = true)
	public PathResponse searchPath(String source, String target, String type) {
		PathGenerator pathGenerator = new PathGenerator(source, target, type);

		List<Line> lines = lineRepository.findAll();
		List<Station> stations = stationRepository.findAll();

		Path path = pathGenerator.generate(lines, stations);
		return new PathResponse(StationResponse.listOf(path.getStationList()), path.distance(),
			path.duration());
	}
}
