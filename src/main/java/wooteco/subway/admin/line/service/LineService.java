package wooteco.subway.admin.line.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.common.exception.SubwayException;
import wooteco.subway.admin.line.domain.line.Line;
import wooteco.subway.admin.line.repository.line.LineRepository;
import wooteco.subway.admin.line.service.dto.line.LineDetailResponse;
import wooteco.subway.admin.line.service.dto.line.LineRequest;
import wooteco.subway.admin.line.service.dto.line.LineResponse;
import wooteco.subway.admin.line.service.dto.lineStation.LineStationCreateRequest;
import wooteco.subway.admin.path.service.dto.WholeSubwayResponse;
import wooteco.subway.admin.station.domain.Station;
import wooteco.subway.admin.station.repository.StationRepository;

@Service
public class LineService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public List<Line> findAll() {
		return lineRepository.findAll();
	}

	@Transactional
	public LineResponse save(Line line) {
		if (lineRepository.findByName(line.getName()).isPresent()) {
			throw new SubwayException("중복된 이름의 호선이 존재합니다.");
		}

		return LineResponse.of(lineRepository.save(line));
	}

	@Transactional
	public void updateLine(Long id, LineRequest request) {
		final Line persistLine = lineRepository.findById(id)
			.orElseThrow(() -> new SubwayException("id에 해당하는 노선이 존재하지 않습니다."));

		persistLine.update(request.toLine());
		lineRepository.save(persistLine);
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	@Transactional
	public void addLineStation(Long id, LineStationCreateRequest request) {
		final Line persistLine = lineRepository.findById(id)
			.orElseThrow(() -> new SubwayException("id에 해당하는 노선이 존재하지 않습니다."));

		persistLine.addLineStation(request.toLineStation());
		lineRepository.save(persistLine);
	}

	@Transactional
	public void delete(Long lineId, Long stationId) {
		final Line persistLine = lineRepository.findById(lineId)
			.orElseThrow(() -> new SubwayException("id에 해당하는 노선이 존재하지 않습니다."));

		persistLine.removeLineStationById(stationId);
		lineRepository.save(persistLine);
	}

	public LineDetailResponse findLineWithStationsById(Long id) {
		final Line line = lineRepository.findById(id)
			.orElseThrow(() -> new SubwayException("id에 해당하는 노선이 존재하지 않습니다."));
		final List<Station> stations = findSortedStations(line.getLineStationsId());

		return LineDetailResponse.of(line, stations);
	}

	private List<Station> findSortedStations(List<Long> lineStationsId) {
		final Map<Long, Station> stations = getStations(lineStationsId);

		return lineStationsId.stream()
			.map(stations::get)
			.collect(toList());
	}

	private Map<Long, Station> getStations(final Iterable<Long> lineStationsId) {
		return stationRepository.findAllById(lineStationsId)
			.stream()
			.collect(toMap(
				Station::getId,
				station -> station)
			);
	}

	public WholeSubwayResponse wholeLines() {
		final List<Line> lines = lineRepository.findAll();
		final Map<Long, Station> stations = getWholeStations(lines);

		return lines.stream()
			.map(line -> getLineDetailResponse(stations, line))
			.collect(collectingAndThen(toList(), WholeSubwayResponse::new));
	}

	private Map<Long, Station> getWholeStations(final List<Line> lines) {
		final Set<Long> stationIds = lines
			.stream()
			.flatMap(line -> line.getLineStationsId().stream())
			.collect(toSet());

		return getStations(stationIds);
	}

	private LineDetailResponse getLineDetailResponse(final Map<Long, Station> stations, Line line) {
		final List<Station> stationsByLine = line.getLineStationsId()
			.stream()
			.map(stations::get)
			.collect(toList());

		return LineDetailResponse.of(line, stationsByLine);
	}

}
