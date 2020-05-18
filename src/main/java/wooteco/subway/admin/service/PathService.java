package wooteco.subway.admin.service;


import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.domain.exception.NoExistStationException;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(String sourceName, String targetName, PathType type) {
        checkDuplicateName(sourceName, targetName);
        Lines lines = new Lines(lineRepository.findAll());
        Stations stations = new Stations(stationRepository.findAll());
        LineStations lineStations = lines.makeLineStation();

        SubwayMap subwayMap = SubwayGraph.makeGraph(type, stations, lineStations);

        Long sourceId = findStationIdByStationName(sourceName);
        Long targetId = findStationIdByStationName(targetName);

        List<Long> shortestPath = subwayMap.findShortestPath(sourceId, targetId);

        Stations pathStations = shortestPath.stream()
                .map(stations::findStation)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));

        int weight = subwayMap.getPathWeight(sourceId, targetId);
        int information = lineStations.getInformation(shortestPath, type);

        if (type.equals(PathType.DISTANCE)) {
            return new PathResponse(StationResponse.listOf(pathStations.getStations()), information, weight);
        }

        return new PathResponse(StationResponse.listOf(pathStations.getStations()), weight, information);
    }

    private void checkDuplicateName(String sourceName, String targetName) {
        if (sourceName.equals(targetName)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다.");
        }
    }

    private Long findStationIdByStationName(String sourceName) {
        return stationRepository.findByName(sourceName)
                .orElseThrow(NoExistStationException::new)
                .getId();
    }
}
