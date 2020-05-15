package wooteco.subway.admin.service;


import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    public static final String NO_EXIST_STATION_ERR_MSG = "존재하지 않는 역은 입력할 수 없습니다.";
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(String sourceName, String targetName, PathType type) {
        checkDuplicateName(sourceName, targetName);
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();

        List<LineStation> lineStations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(lineStation -> lineStation.getPreStationId() != null)
                .collect(Collectors.toList());

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph(type, stations, lineStations);

        Long source = findStationIdWithStationName(sourceName, stations);
        Long target = findStationIdWithStationName(targetName, stations);

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);

        List<Long> shortestPath = findShortestPath(source, target, dijkstraShortestPath);

        List<Station> pathStations = shortestPath.stream()
                .map(id -> findStation(stations, id))
                .collect(Collectors.toList());

        int weight = (int) dijkstraShortestPath.getPathWeight(source, target);

        int information = lineStations.stream()
                .filter(lineStation -> shortestPath.contains(lineStation.getStationId()))
                .filter(lineStation -> shortestPath.contains(lineStation.getPreStationId()))
                .mapToInt(type::getGetInformation)
                .sum();

        if (type.equals(PathType.DISTANCE)) {
            return new PathResponse(StationResponse.listOf(pathStations), information, weight);
        }

        return new PathResponse(StationResponse.listOf(pathStations), weight, information);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(PathType type, List<Station> stations, List<LineStation> lineStations) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getId());
        }

        for (LineStation station : lineStations) {
            graph.setEdgeWeight(graph.addEdge(station.getPreStationId(), station.getStationId()), type.getGetWeight(station));
        }

        return graph;
    }

    private List<Long> findShortestPath(Long source, Long target, DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath) {
        List<Long> shortestPath;
        try {
            shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다. 노선도를 확인해주세요.");
        }
        return shortestPath;
    }

    private void checkDuplicateName(String sourceName, String targetName) {
        if (sourceName.equals(targetName)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다.");
        }
    }

    private Long findStationIdWithStationName(String sourceName, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.getName().equals(sourceName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_STATION_ERR_MSG)).getId();
    }

    private Station findStation(List<Station> stations, Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_STATION_ERR_MSG));
    }
}
