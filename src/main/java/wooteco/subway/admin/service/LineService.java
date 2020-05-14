package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.dto.line.LineDetailResponse;
import wooteco.subway.admin.dto.line.LineRequest;
import wooteco.subway.admin.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> showLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line persistLine = findLineBy(id);
        persistLine.update(lineRequest.toLine());
        lineRepository.save(persistLine);
    }

    private Line findLineBy(Long id) {
        return lineRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest lineStationCreateRequest) {
        Line line = findLineBy(id);
        LineStation lineStation = lineStationCreateRequest.toLineStation();

        line.addLineStation(lineStation);
        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineBy(lineId);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = findLineBy(id);
        List<Station> stations = stationService.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    public WholeSubwayResponse findWholeLines() {
        List<Line> lines = lineRepository.findAll();
        List<LineDetailResponse> lineDetailResponses =
                lines.stream().map(line ->
                        LineDetailResponse.of(line, stationService.findAllById(line.getLineStationsId())))
                        .collect(Collectors.toList());
        return WholeSubwayResponse.of(lineDetailResponses);
    }
}
