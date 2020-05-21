package wooteco.subway.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.GraphService;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.exception.NonExistentDataException;
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
		Line persistLine = lineRepository.findById(id)
			.orElseThrow(() -> new NonExistentDataException("존재하지 않는 Line입니다."));
		persistLine.update(request.toLine());
		lineRepository.save(persistLine);
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long id, LineStationCreateRequest request) {
		Line line = lineRepository.findById(id)
			.orElseThrow(() -> new NonExistentDataException("존재하지 않는 Line입니다."));
		LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
			request.getDistance(), request.getDuration());
		line.addLineStation(lineStation);

		lineRepository.save(line);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = lineRepository.findById(lineId)
			.orElseThrow(() -> new NonExistentDataException("존재하지 않는 Line입니다."));
		line.removeLineStationById(stationId);
		lineRepository.save(line);
	}

	public LineDetailResponse findLineWithStationsById(Long id) {
		Line persistLine = lineRepository.findById(id)
			.orElseThrow(() -> new NonExistentDataException("존재하지 않는 Line입니다."));
		List<Station> stations = persistLine.getLineStationsId()
			.stream()
			.map(stationId -> stationRepository.findById(stationId))
			.map(station -> station.orElseThrow(() -> new NonExistentDataException("존재하지 않는 Station입니다.")))
			.collect(Collectors.toList());

		return LineDetailResponse.of(persistLine, StationResponse.listOf(stations));
	}

	public WholeSubwayResponse wholeLines() {
		List<Line> lines = lineRepository.findAll();

		return lines.stream()
			.map(line -> findLineWithStationsById(line.getId()))
			.collect(Collectors.collectingAndThen(Collectors.toList(), WholeSubwayResponse::of));
	}

	public List<PathResponse> findAllPath(String departStationName, String arrivalStationName) {
		GraphService graphService = new GraphService();
		if (departStationName.equals(arrivalStationName)) {
			throw new IllegalArgumentException("출발지와 도착지는 같을 수 없습니다.");
		}
		Long departStationId = getStationByName(departStationName).getId();
		Long arrivalStationId = getStationByName(arrivalStationName).getId();
		Stations stations = Stations.of(stationRepository.findAll());
		Lines lines = Lines.of(lineRepository.findAll());

		return graphService.findAllPath(stations, lines, departStationId, arrivalStationId);
	}

	private Station getStationByName(String departStationName) {
		return stationRepository.findByName(departStationName)
			.orElseThrow(() -> new NonExistentDataException(String.format("%s는 존재하지 않는 데이터입니다.", departStationName)));
	}
}
