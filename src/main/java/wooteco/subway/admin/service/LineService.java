package wooteco.subway.admin.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Line persistLine = lineRepository.save(lineRequest.toLine());
        Line line = findBy(persistLine.getId());

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> showLines() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.listOf(lines);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line persistLine = findBy(id);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    @Transactional
    public void deleteLineBy(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public void addLineStation(Long lineId, LineStationCreateRequest request) {
        Line line = findBy(lineId);
        LineStation lineStation = request.toLineStation();

        line.addLineStation(lineStation);
        lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    private Line findBy(Long lineId) {
        return lineRepository.findById(lineId)
            .orElseThrow(() -> new NoSuchElementException("노선을 찾을 수 없습니다."));
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findBy(lineId);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = findBy(lineId);
        List<Long> lineStationsIds = line.getLineStationsIds();
        return LineDetailResponse.of(line, sortBySubwayRule(lineStationsIds));
    }

    @Transactional(readOnly = true)
    public List<LineDetailResponse> wholeLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(line -> LineDetailResponse.of(line, sortBySubwayRule(line.getLineStationsIds())))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Station> sortBySubwayRule(List<Long> lineStationsIds) {
        List<Station> stations = stationRepository.findAllById(lineStationsIds);

        return lineStationsIds.stream()
            .map(lineStationsId -> getStationByEqualId(stations, lineStationsId))
            .collect(Collectors.toList());
    }

    private Station getStationByEqualId(List<Station> stations, Long lineStationsId) {
        return stations.stream()
            .filter(station -> lineStationsId.equals(station.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다"));
    }
}
