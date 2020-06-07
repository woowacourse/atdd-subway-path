package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.errors.PathException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {
    private static final String KOREAN_WORD = "^[가-힣]*$";
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;

    private StationRepository stationRepository;
    private LineRepository lineRepository;
    private GraphService graphService;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, GraphService graphService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graphService = graphService;
    }

    public PathResponse findPath(String source, String target, PathType type) {
        checkSameStationName(source, target);
        checkKoreanStationName(source, target, KOREAN_WORD);

        List<Line> lines = lineRepository.findAll();

        Station sourceStation = findStationByName(source);
        Station targetStation = findStationByName(target);

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);
        List<Station> stations = stationRepository.findAllById(path);
        List<LineStation> lineStations = calculateLineStations(lines);
        List<LineStation> paths = extractPathLineStation(path, lineStations);

        checkEmptyPath(paths);

        int duration = 0;
        int distance = 0;
        for (LineStation lineStation : paths) {
            duration += lineStation.getDuration();
            distance += lineStation.getDistance();
        }

        List<Station> pathStation = calculateStationPath(path, stations);

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }

    private List<Station> calculateStationPath(List<Long> path, List<Station> stations) {
        return path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList());
    }

    private void checkEmptyPath(List<LineStation> paths) {
        if (paths.isEmpty()) {
            throw new PathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

    private List<LineStation> calculateLineStations(List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .collect(Collectors.toList());
    }

    private Station findStationByName(String source) {
        return stationRepository.findByName(source)
                .orElseThrow(() -> new PathException("해당 역을 찾을 수 없습니다."));
    }

    private void checkKoreanStationName(String source, String target, String regExp) {
        if (!source.matches(regExp) || !target.matches(regExp)) {
            throw new PathException("출발역과 도착역은 한글만 입력가능합니다.");
        }
    }

    private void checkSameStationName(String source, String target) {
        if (Objects.equals(source, target)) {
            throw new PathException("출발역과 도착역은 같은 지하철역이 될 수 없습니다.");
        }
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(it -> it.getId().equals(stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<LineStation> extractPathLineStation(List<Long> path, List<LineStation> lineStations) {
        Long preStationId = setupPreStationId(path);
        List<LineStation> paths = new ArrayList<>();

        for (int i = SECOND_INDEX; i < path.size(); i++) {
            Long stationId = path.get(i);
            Long finalPreStationId = preStationId;
            LineStation lineStation = calculateLineStation(lineStations, stationId, finalPreStationId);

            paths.add(lineStation);
            preStationId = stationId;
        }

        return paths;
    }

    private Long setupPreStationId(List<Long> path) {
        return path.isEmpty() ? null : path.get(FIRST_INDEX);
    }

    private LineStation calculateLineStation(List<LineStation> lineStations, Long stationId, Long finalPreStationId) {
        return lineStations.stream()
                .filter(it -> it.isLineStationOf(finalPreStationId, stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
