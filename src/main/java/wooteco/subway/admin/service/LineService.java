package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.EdgeCreateRequest;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineWithStationsResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.exception.LineNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

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
        Line persistLine = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addEdge(Long id, EdgeCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.addEdge(request.toEdge());
        lineRepository.save(line);
    }

    public void removeEdge(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        line.removeEdgeByStationId(stationId);
        lineRepository.save(line);
    }

    public LineWithStationsResponse findLineWithStationsById(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        List<Station> stations = stationRepository.findAllById(line.getSortedStationIds());
        return LineWithStationsResponse.of(line, stations);
    }

    public WholeSubwayResponse wholeLines() {
        Lines wholeLines = new Lines(lineRepository.findAll());
        List<Long> wholeStationIds = wholeLines.getStationIds();
        Stations stations = new Stations(stationRepository.findAllById(wholeStationIds));

        List<LineWithStationsResponse> lineWithStationsResponses = wholeLines.findLineWithStationsResponses(stations);

        return WholeSubwayResponse.of(lineWithStationsResponses);
    }
}
