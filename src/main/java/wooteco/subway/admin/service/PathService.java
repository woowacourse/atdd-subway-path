package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.SearchPathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.LineStationRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private LineStationRepository lineStationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, LineStationRepository lineStationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineStationRepository = lineStationRepository;
    }

    public SearchPathResponse searchPath(String startStationName, String targetStationName, String type) {
        List<Line> lines = lineRepository.findAll();
        List<Station> stations = stationRepository.findAll();

        Station startStation = findStationByName(startStationName);
        Station targetStation = findStationByName(targetStationName);

        Graph graph = Graph.of(lines,stations,type);
        GraphPath<Station, Edge> shortestPath = graph.findShortestPath(startStation, targetStation);

        int durationSum = shortestPath.getEdgeList().stream()
                .mapToInt(edge->edge.getDuration())
                .sum();
        int distanceSum = shortestPath.getEdgeList().stream()
                .mapToInt(edge -> edge.getDistance())
                .sum();
        List<String> stationNames = shortestPath.getVertexList().stream()
                .map(edge->edge.getName())
                .collect(Collectors.toList());

        return new SearchPathResponse(durationSum, distanceSum, stationNames);
    }

    private Station findStationByName(String startStationName) {
        return stationRepository.findByName(startStationName)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }
}
