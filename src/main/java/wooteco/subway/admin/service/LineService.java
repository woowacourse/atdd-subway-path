package wooteco.subway.admin.service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

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

    public LineResponse addLine(final LineRequest lineRequest) {
        Line line = lineRequest.toLine();
        Line persistLine = save(line);

        return LineResponse.of(persistLine);
    }

    public List<LineResponse> showLines() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.listOf(lines);
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public LineResponse addLineStation(Long lineId, LineStationCreateRequest request) {
        Line line = findBy(lineId);
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        return LineResponse.of(save(line));
    }

    public Line findBy(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findBy(lineId);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = findBy(lineId);
        List<Station> stations = stationRepository.findAll();

        return LineDetailResponse.of(line, sortBySubwayRule(line.getLineStationsIds(), stations));
    }

    public List<LineDetailResponse> wholeLines() {
        List<Line> lines = lineRepository.findAll();
        Set<Long> allStationOfLines = lines.stream()
            .flatMap(line -> line.getLineStationsIds().stream())
            .collect(Collectors.toSet());

        List<Station> stations = stationRepository.findAllById(allStationOfLines);

        return lines.stream()
            .map(line -> LineDetailResponse.of(line,
                sortBySubwayRule(line.getLineStationsIds(), stations)))
            .collect(Collectors.toList());
    }

    private List<Station> sortBySubwayRule(List<Long> lineStationsIds, List<Station> stations) {
        return stations.stream()
            .filter(station -> lineStationsIds.contains(station.getId()))
            .sorted(Comparator.comparing(station -> lineStationsIds.indexOf(station.getId())))
            .collect(Collectors.toList());
    }
}
