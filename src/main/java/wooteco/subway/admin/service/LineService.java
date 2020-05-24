package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.exception.NoSuchValueException;
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

    public Line save(LineRequest request) {
        return lineRepository.save(request.toLine());
    }

    @Transactional(readOnly = true)
    public List<LineResponse> showLines() {
        return LineResponse.listOf(lineRepository.findAll());
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = findLine(id);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        validateStations(request.getPreStationId(), request.getStationId());
        Line line = findLine(id);
        LineStation lineStation = LineStation.of(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLine(lineId);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = findLine(id);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Station::getId, station -> station));
        List<LineDetailResponse> responses = lines.stream()
            .map(line -> getLineDetailResponse(stations, line))
            .collect(Collectors.toList());
        return WholeSubwayResponse.of(responses);
    }

    private void validateStations(Long preStationId, Long stationId) {
        if (Objects.nonNull(preStationId) && !stationRepository.existsById(preStationId)) {
            throw new NoSuchValueException("존재하지 않는 이전역입니다.");
        }
        if (Objects.isNull(stationId) || !stationRepository.existsById(stationId)) {
            throw new NoSuchValueException("존재하지 않는 다음역입니다.");
        }
    }

    private LineDetailResponse getLineDetailResponse(Map<Long, Station> stations, Line line) {
        List<Station> stationsList = line.stationsIdStream()
            .map(stations::get)
            .collect(Collectors.toList());
        return LineDetailResponse.of(line, stationsList);
    }

    private Line findLine(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchValueException("존재하지 않는 노선입니다."));
    }
}
