package wooteco.subway.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.exception.NotFoundLineException;
import wooteco.subway.admin.repository.LineRepository;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository,
        StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse save(Line line) {
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> showLines() {
        return LineResponse.listOf(lineRepository.findAll());
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundLineException(id));
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
        List<Station> stations = stationService.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        List<LineDetailResponse> lineDetailResponses = lines.stream()
            .map(line ->
                LineDetailResponse.of(line, stationService.findAllById(line.getLineStationsId())))
            .collect(Collectors.toList());
        return WholeSubwayResponse.of(lineDetailResponses);
    }
}
