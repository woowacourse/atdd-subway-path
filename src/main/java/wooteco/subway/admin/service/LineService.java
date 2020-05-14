package wooteco.subway.admin.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;
	private GraphService graphService;

	public LineService(LineRepository lineRepository, StationRepository stationRepository, GraphService graphService) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.graphService = graphService;
	}

	public Line save(Line line) {
		return lineRepository.save(line);
	}

	public List<Line> showLines() {
		return lineRepository.findAll();
	}

	public void updateLine(Long id, LineRequest request) {
		Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		persistLine.update(request.toLine());
		lineRepository.save(persistLine);
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long id, LineStationCreateRequest request) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
			request.getDistance(), request.getDuration());
		line.addLineStation(lineStation);

		lineRepository.save(line);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
		line.removeLineStationById(stationId);
		lineRepository.save(line);
	}

	public LineDetailResponse findLineWithStationsById(Long id) {
		Line line = lineRepository.findById(id)
			.orElseThrow(RuntimeException::new);
		List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
		return LineDetailResponse.of(line, stations);
	}

	public WholeSubwayResponse wholeLines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(line -> findLineWithStationsById(line.getId()))
			.collect(Collectors.collectingAndThen(Collectors.toList(), WholeSubwayResponse::of));
	}

	public PathResponse searchPath(String source, String target, PathType pathType) {
		List<Line> lines = lineRepository.findAll();
		Station sourceStation = stationRepository.findByName(source).orElseThrow(RuntimeException::new);
		Station targetStation = stationRepository.findByName(target).orElseThrow(RuntimeException::new);

		List<Edge> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), pathType);

		List<Long> ids = lines.stream()
			.map(Line::getLineStationsId)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
		Map<Long, Station> map = stationRepository.findAllById(ids)
			.stream()
			.collect(Collectors.toMap(Station::getId, Function
				.identity()));
		List<Station> stations = path.stream()
			.map(Edge::getStationId)
			.map(map::get)
			.collect(Collectors.toList());
		int distance = path.stream()
			.mapToInt(Edge::getDistance)
			.sum();
		int duration = path.stream()
			.mapToInt(Edge::getDuration)
			.sum();
		return PathResponse.of(stations, distance, duration);
	}
}
