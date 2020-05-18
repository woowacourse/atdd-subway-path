package wooteco.subway.admin.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.req.LineRequest;
import wooteco.subway.admin.dto.req.LineStationCreateRequest;
import wooteco.subway.admin.dto.res.LineDetailResponse;
import wooteco.subway.admin.dto.res.WholeSubwayResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
@Transactional
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        List<Long> wholeStationIds = getWholeStationIds(lines);
        List<Station> wholeStations = stationRepository.findAllById(wholeStationIds);

        List<LineDetailResponse> lineDetailResponses = getLineDetailResponses(lines, wholeStations);
        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private List<Long> getWholeStationIds(List<Line> lines) {
        return lines.stream()
            .map(Line::getLineStationsId)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private List<LineDetailResponse> getLineDetailResponses(List<Line> lines,
        List<Station> wholeStations) {
        return lines.stream()
            .map(line -> LineDetailResponse.of(line, line.getMatchingStations(wholeStations)))
            .collect(Collectors.toList());
    }
}
