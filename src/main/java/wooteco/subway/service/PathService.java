package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Graphs;
import wooteco.subway.domain.path.Path;
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
		createGraphs(lineRepository.findAll(), stationRepository.findAll());
	}

	private void createGraphs(List<Line> lines, List<Station> stations) {
		Graphs.getInstance().create(lines, stations);
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
		Path path = Graphs.getInstance().findPath(source, target, type);
		return new PathResponse(StationResponse.listOf(path.getStationList()), path.distance(),
			path.duration());
	}
}
