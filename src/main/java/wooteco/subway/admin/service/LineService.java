package wooteco.subway.admin.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.CustomException;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.path.PathType;
import wooteco.subway.admin.domain.path.ShortestPath;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final String NO_SUCH_LINE_EXCEPTION = "노선이 존재하지 않습니다.";
    private final String NO_SUCH_STATION_EXCEPTION = "존재하지 않는 역입니다.";
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Line save(Line line) {
        try {
            return lineRepository.save(line);
        } catch (DbActionExecutionException exception) {
            if (exception.getCause() instanceof DuplicateKeyException) {
                throw new CustomException("이미 존재하는 호선입니다.", exception);
            }
            if (exception.getCause() instanceof DataIntegrityViolationException) {
                throw new CustomException("필수값을 입력해주세요.", exception);
            }
            throw exception;
        }
    }

    public List<Line> showLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id)
                .orElseThrow(() -> new CustomException(NO_SUCH_LINE_EXCEPTION, new NoSuchElementException()));
        persistLine.update(request.toLine());
        save(persistLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new CustomException(NO_SUCH_LINE_EXCEPTION, new NoSuchElementException()));
        LineStation lineStation = new LineStation(request.getPreStationId(), request.getStationId(), request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NO_SUCH_LINE_EXCEPTION, new NoSuchElementException()));
        line.removeLineStationById(stationId);
        save(line);
    }

    public LineDetailResponse findDetailLineById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new CustomException(NO_SUCH_LINE_EXCEPTION, new NoSuchElementException()));
        List<Station> stations = stationRepository.findAllById(line.getLineStationsId());

        List<Station> orderedStations = new ArrayList<>();
        for (Long stationId : line.getLineStationsId()) {
            stations.stream()
                    .filter(station -> station.getId().equals(stationId))
                    .findAny()
                    .ifPresent(orderedStations::add);
        }
        return LineDetailResponse.of(line, orderedStations);
    }

    public List<LineDetailResponse> findDetailLines() {
        List<LineDetailResponse> response = new ArrayList<>();
        for (Line line : lineRepository.findAll()) {
            List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
            response.add(LineDetailResponse.of(line, stations));
        }
        return response;
    }

    public PathResponse findShortestPath(String sourceName, String targetName, PathType pathType) {
        if (sourceName.equals(targetName)) {
            throw new CustomException("출발역과 도착역이 같습니다.", new NoSuchElementException());
        }
        Long sourceId = stationRepository.findIdByName(sourceName)
                .orElseThrow(() -> new CustomException(NO_SUCH_STATION_EXCEPTION, new NoSuchElementException()));
        Long targetId = stationRepository.findIdByName(targetName)
                .orElseThrow(() -> new CustomException(NO_SUCH_STATION_EXCEPTION, new NoSuchElementException()));

        List<LineStation> lineStations = findAllLineStations();
        ShortestPath shortestPath = ShortestPath.of(lineStations, pathType);

        List<Long> pathStationIds = shortestPath.getVertexList(sourceId, targetId);
        int distance = shortestPath.getDistance(sourceId, targetId);
        int duration = shortestPath.getDuration(sourceId, targetId);

        List<String> pathStationNames = stationRepository.findAllNameById(pathStationIds);
        return new PathResponse(distance, duration, pathStationNames);
    }

    private List<LineStation> findAllLineStations() {
        return Collections.unmodifiableList(lineRepository.findAll())
                .stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
