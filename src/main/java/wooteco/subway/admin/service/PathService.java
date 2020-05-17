package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static wooteco.subway.admin.domain.EdgeType.DISTANCE;
import static wooteco.subway.admin.domain.EdgeType.DURATION;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SearchPathResponse searchPath(String startStationName, String targetStationName, String type) {
        validateTwoStation(startStationName, targetStationName);

        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();
        Station startStation = findStationByName(startStationName, stations);
        Station targetStation = findStationByName(targetStationName, stations);

        Graph graph = Graph.of(lines, stations, EdgeType.of(type));
        GraphPath<Station, Edge> shortestPath = graph.findShortestPath(startStation, targetStation);

        validatePath(shortestPath);

        int durationSum = getEdgeValueSum(shortestPath, DURATION);
        int distanceSum = getEdgeValueSum(shortestPath, DISTANCE);
        List<String> stationNames = shortestPath.getVertexList().stream()
                .map(edge -> edge.getName())
                .collect(Collectors.toList());

        return new SearchPathResponse(durationSum, distanceSum, stationNames);
    }

    private int getEdgeValueSum(GraphPath<Station, Edge> shortestPath, EdgeType edgeType) {
        return shortestPath.getEdgeList().stream()
                .mapToInt(edge -> edgeType.getEdgeValue(edge.toLineStation()))
                .sum();
    }

    private Station findStationByName(String stationName, List<Station> stations) {
        return stations.stream()
                .filter(station -> stationName.equals(station.getName()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }

    private void validateTwoStation(String startStationName, String targetStationName) {
        if (startStationName.equals(targetStationName)) {
            throw new IllegalArgumentException("시작역과 도착역이 같습니다.");
        }
    }

    private void validatePath(GraphPath<Station, Edge> path) {
        if (path == null) {
            throw new IllegalArgumentException("두 역이 연결되어있지 않습니다.");
        }
    }
}
