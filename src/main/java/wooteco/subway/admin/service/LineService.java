package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.*;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineRequest.toLine());
        return LineResponse.from(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> showLines() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.listFrom(lines);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line line = getLine(id);
        line.update(request.toLine());
        lineRepository.save(line);
    }

    private Line getLine(final Long id) {
        return lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addLineStation(Long id, LineStationCreateRequest lineStationRequest) {
        Line line = getLine(id);
        LineStation lineStation = lineStationRequest.toLineStation();
        line.addLineStation(lineStation);
        lineRepository.save(line);
    }

    @Transactional
    public void removeLineStation(final Long id, final Long stationId) {
        Line line = getLine(id);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(final Long id) {
        Line line = getLine(id);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        return WholeSubwayResponse.from(
                lines.stream()
                        .map(line -> LineDetailResponse.of(
                                line, stationRepository.findAllById(line.getLineStationsId())))
                        .collect(Collectors.toList()));
    }
}
