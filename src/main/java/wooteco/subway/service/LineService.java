package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.LineStation;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineStationCreateRequest;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class LineService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

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
}
