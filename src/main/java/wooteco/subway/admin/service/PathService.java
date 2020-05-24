package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Graphs;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.vo.PathType;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.exception.EntityNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final Graphs graphs;

    public PathService(LineRepository lineRepository,
        StationRepository stationRepository, Graphs graphs) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.graphs = graphs;
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
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

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
        return LineDetailResponse.of(line, stations);
    }

    @Transactional
    public void addLineStation(Long id, LineStationCreateRequest request) {
        validateStations(request.getPreStationId(), request.getStationId());
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        LineStation lineStation = LineStation.of(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
        synchronized (graphs) {
            graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
        }
    }

    @Transactional
    public synchronized void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
        synchronized (graphs) {
            graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
        }
    }

    @Transactional
    public PathResponse findPath(Long sourceId, Long targetId, PathType pathType) {
        validate(sourceId, targetId);
        return graphs.getPath(sourceId, targetId, pathType);
    }

    private void validate(Long sourceId, Long targetId) {
        validateEmpty(sourceId, targetId);
        validateSameIds(sourceId, targetId);
    }

    private void validateEmpty(Long sourceId, Long targetId) {
        if (Objects.isNull(sourceId) || Objects.isNull(targetId)) {
            throw new IllegalArgumentException("소스와 타겟 정보가 비어있습니다.");
        }
    }

    private void validateSameIds(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private LineDetailResponse getLineDetailResponse(Map<Long, Station> stations, Line line) {
        List<Long> stationIds = line.getLineStationsId();
        List<Station> stationsList = stationIds.stream()
            .map(stations::get)
            .collect(Collectors.toList());
        return LineDetailResponse.of(line, stationsList);
    }

    private void validateStations(Long preStationId, Long stationId) {
        if (Objects.nonNull(preStationId) && !stationRepository.existsById(preStationId)) {
            throw new EntityNotFoundException(Station.class.getName(), preStationId);
        }
        if (Objects.isNull(stationId) || !stationRepository.existsById(stationId)) {
            throw new EntityNotFoundException(Station.class.getName(), stationId);
        }
    }
}
