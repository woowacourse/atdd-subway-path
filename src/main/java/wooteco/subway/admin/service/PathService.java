package wooteco.subway.admin.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineService lineService;

    public PathService(LineRepository lineRepository,
        StationRepository stationRepository, LineService lineService) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineService = lineService;
    }

    public PathResponse showPaths(String source, String target) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(
            DefaultWeightedEdge.class);
        List<Line> lines = lineRepository.findAll();
        List<Long> wholeStationIds = getWholeStationIds(lines);
        List<Station> wholeStations = stationRepository.findAllById(wholeStationIds);
        Set<Station> collect = new HashSet<>(wholeStations);
        for (Station station : collect) {
            graph.addVertex(station.getName());
        }
        graph.setEdgeWeight(graph.addEdge());

        DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        return stationRepository.findByNames(shortestPath);
    }

    private List<Long> getWholeStationIds(List<Line> lines) {
        return lines.stream()
            .map(Line::getLineStationsId)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
