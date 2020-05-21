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

    private GraphService graphService;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, GraphService graphService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.graphService = graphService;
    }

    public PathResponse findPath(String sourceName, String targetName, PathType type) {
        Long source = null;
        Long target = null;
        checkDuplicateName(sourceName, targetName);
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();

        List<LineStation> lineStations = lines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .filter(lineStation -> lineStation.getPreStationId() != null)
                .collect(Collectors.toList());

        for (Station station : stations) {
            source = findIdByName(sourceName, source, station);
            target = findIdByName(targetName, target, station);
        }

        validateStations(source, target);


        WeightedMultigraph<Long, DefaultWeightedEdge> graph = graphService.makeGraph(type, stations, lineStations);

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);

        List<Long> shortestPath = graphService.findShortestPath(source, target, dijkstraShortestPath);

        List<Station> pathStations = shortestPath.stream()
                .map(id -> findStation(stations, id))
                .collect(Collectors.toList());

        int weight = (int) dijkstraShortestPath.getPathWeight(source, target);

        int extraInformation = lineStations.stream()
                .filter(lineStation -> shortestPath.contains(lineStation.getStationId()) &&
                        shortestPath.contains(lineStation.getPreStationId()))
                .mapToInt(type::getExtraInformation)
                .sum();

        if (type.equals(PathType.DISTANCE)) {
            return new PathResponse(StationResponse.listOf(pathStations), extraInformation, weight);
        }

        return new PathResponse(StationResponse.listOf(pathStations), weight, extraInformation);
    }

    private void validateStations(Long source, Long target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException(NO_EXIST_STATION_ERR_MSG);
        }
    }

    private Long findIdByName(String name, Long id, Station station) {
        if (station.getName().equals(name)) {
            return station.getId();
        }
        return id;
    }

    private void checkDuplicateName(String sourceName, String targetName) {
        if (sourceName.equals(targetName)) {
            throw new IllegalArgumentException("출발역과 도착역은 동일할 수 없습니다.");
        }
    }

    private Station findStation(List<Station> stations, Long id) {
        return stations.stream()
                .filter(station -> station.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_STATION_ERR_MSG));
    }
}
