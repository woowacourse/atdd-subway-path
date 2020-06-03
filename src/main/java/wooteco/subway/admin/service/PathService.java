package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.errors.PathException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(ShortestPathResponse shortestPathResponse) {
        Graph graph = new Graph();
        String source = shortestPathResponse.getSource();
        String target = shortestPathResponse.getTarget();
        String pathType = shortestPathResponse.getPathType();

        checkSameStationName(source, target);

        List<Line> lines = lineService.findAll();
        Station sourceStation = findStationByName(source);
        Station targetStation = findStationByName(target);
        List<Long> path = graph.findPath(lines, sourceStation.getId(), targetStation.getId(), PathType.valueOf(pathType));
        List<Station> stations = stationService.findAllById(path);
        List<LineStation> lineStations = findLineStationsWithOutSourceLineStation(lines);
        List<LineStation> paths = extractPathLineStation(path, lineStations);

        int duration = calculateFastestDuration(paths);
        int distance = calculateShortestDistance(paths);

        List<Station> pathStation = findStationPath(path, stations);

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }

    private List<Station> findStationPath(List<Long> path, List<Station> stations) {
        return path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList());
    }

    private int calculateShortestDistance(List<LineStation> paths) {
        return paths.stream()
                .mapToInt(LineStation::getDistance)
                .sum();
    }

    private int calculateFastestDuration(List<LineStation> paths) {
        return paths.stream()
                .mapToInt(LineStation::getDuration)
                .sum();
    }

    private List<LineStation> findLineStationsWithOutSourceLineStation(List<Line> lines) {
        List<LineStation> lineStations = new ArrayList<>();
        lines.stream()
                .map(Line::lineStationsWithOutSourceLineStation)
                .forEach(lineStations::addAll);
        return lineStations;
    }

    private Station findStationByName(String source) {
        return stationService.findByName(source);
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
        List<LineStation> paths = new ArrayList<>();

        for (int i = 1; i < path.size(); i++) {
            Long stationId = path.get(i);
            Long finalPreStationId = path.get(i - 1);
            LineStation lineStation = calculateLineStation(lineStations, stationId, finalPreStationId);

            paths.add(lineStation);
        }

        return paths;
    }

    private LineStation calculateLineStation(List<LineStation> lineStations, Long stationId, Long finalPreStationId) {
        return lineStations.stream()
                .filter(it -> it.isLineStationOf(finalPreStationId, stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
