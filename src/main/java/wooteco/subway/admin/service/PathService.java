package wooteco.subway.admin.service;


import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<Station> stations = stationRepository.findAll();
        for (Station station : stations) {
            graph.addVertex(station.getId());
        }
        List<Line> lines = lineRepository.findAll();
        List<LineStation> lineStations = new ArrayList<>();
        for (Line line : lines) {
            lineStations.addAll(line.getStations());
        }

        for (LineStation station : lineStations) {
            if (Objects.isNull(station.getPreStationId())) {
                continue;
            }
            graph.setEdgeWeight(graph.addEdge(station.getPreStationId(), station.getStationId()), station.getDistance());
        }

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        List<Long> shortestPath
                = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();
        int distance = (int) dijkstraShortestPath.getPathWeight(sourceId, targetId);
        List<Station> pathStations = stations.stream()
                .filter(station -> shortestPath.contains(station.getId()))
                .collect(Collectors.toList());

        int duration = lineStations.stream()
                .filter(lineStation -> shortestPath.contains(lineStation.getStationId()))
                .filter(lineStation -> shortestPath.contains(lineStation.getPreStationId()))
                .mapToInt(lineStation -> lineStation.getDuration())
                .sum();

        return new PathResponse(StationResponse.listOf(pathStations), duration, distance);
    }
}
