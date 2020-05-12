package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {

	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
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
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
		return LineDetailResponse.of(line, stations);
	}

	public WholeSubwayResponse wholeLines() {
		List<Line> lines = lineRepository.findAll();
		Map<Long, Station> stations = getWholeStations(lines);

		List<LineDetailResponse> lineDetailResponses = lines.stream()
			.map(line -> getLineDetailResponse(stations, line))
			.collect(Collectors.toList());
		return new WholeSubwayResponse(lineDetailResponses);
	}

	private Map<Long, Station> getWholeStations(List<Line> lines) {
		Set<Long> stationIds = lines.stream()
			.flatMap(line -> line.getLineStationsId().stream())
			.collect(Collectors.toSet());

		return getStations(stationIds);
	}

	private Map<Long, Station> getStations(Set<Long> stationIds) {
		return stationRepository.findAllById(stationIds)
			.stream()
			.collect(Collectors.toMap(
				Station::getId,
				station -> station
			));
	}

	private LineDetailResponse getLineDetailResponse(Map<Long, Station> stations, Line line) {
		return LineDetailResponse.of(line,
			line.getLineStationsId()
				.stream()
				.map(stations::get)
				.collect(Collectors.toList()));
	}

}
