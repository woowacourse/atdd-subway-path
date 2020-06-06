package wooteco.subway.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.LineStation;
import wooteco.subway.admin.domain.entity.Station;
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

	@Transactional
	public Line save(Line line) {
		return lineRepository.save(line);
	}

	@Transactional(readOnly = true)
	public List<Line> showLines() {
		return lineRepository.findAll();
	}

	@Transactional
	public void updateLine(Long id, LineRequest request) {
		Line persistLine = findLineById(id);
		persistLine.update(request.toLine());
		lineRepository.save(persistLine);
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	@Transactional
	public void addLineStation(Long id, LineStationCreateRequest request) {
		Line line = findLineById(id);
		LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
			request.getDistance(), request.getDuration());
		line.addLineStation(lineStation);

		lineRepository.save(line);
	}

	@Transactional
	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		line.removeLineStationById(stationId);
		lineRepository.save(line);
	}

	@Transactional(readOnly = true)
	public LineDetailResponse findLineWithStationsById(Long id) {
		Line line = findLineById(id);
		List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
		return LineDetailResponse.of(line, stations);
	}

	@Transactional(readOnly = true)
	public WholeSubwayResponse wholeLines() {
		List<LineDetailResponse> lineDetailResponses = new ArrayList<>();

		List<Line> lines = lineRepository.findAll();
		for (Line line : lines) {
			List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
			lineDetailResponses.add(LineDetailResponse.of(line, stations));
		}

		return WholeSubwayResponse.of(lineDetailResponses);
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다 id=" + id));
	}
}
