package wooteco.subway.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.SubwayShortestPath;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.exceptions.NotFoundException;

@Service
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository,
		StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public Line save(Line line) {
		return lineRepository.save(line);
	}

	public List<Line> showLines() {
		return lineRepository.findAll();
	}

	public void updateLine(Long id, LineRequest request) {
		final Line persistLine = lineRepository.findById(id)
			.orElseThrow(RuntimeException::new)
			.update(request.toLine().withId(id));
		lineRepository.save(persistLine);
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long id, LineStationCreateRequest request) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		LineStation lineStation = new LineStation(request.getPreStationId(),
			request.getStationId(),
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
			.collect(Collectors.collectingAndThen(Collectors.toList(),
				WholeSubwayResponse::of));
	}

	public PathResponse searchPath(String source, String target, PathType pathType) {
		List<Line> lines = lineRepository.findAll();
		List<Station> stations = stationRepository.findAll();
		Station sourceStation = stationRepository.findByName(source)
			.orElseThrow(() -> new NotFoundException("출발역을 찾을 수 없습니다."));
		Station targetStation = stationRepository.findByName(target)
			.orElseThrow(() -> new NotFoundException("도착역을 찾을 수 없습니다."));

		SubwayShortestPath subwayShortestPath = SubwayShortestPath.of(lines, stations,
			sourceStation, targetStation, pathType);

		return PathResponse.of(subwayShortestPath.getVertexList(),
			subwayShortestPath.getDistance(),
			subwayShortestPath.getDuration());
	}
}
