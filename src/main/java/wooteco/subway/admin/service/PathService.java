package wooteco.subway.admin.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.domain.PathWeightedEdge;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PathService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathSearchResponse searchPath(String source, String target, PathType type) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("Source can not be same with target.");
        }

        WeightedMultigraph<String, PathWeightedEdge> pathGraph = getPathWeightedMultiGraph(type);

        GraphPath<String, PathWeightedEdge> path = DijkstraShortestPath.findPathBetween(pathGraph, source, target);
        if (Objects.isNull(path)) {
            throw new IllegalArgumentException("Can not find path between " + source + "," + target + ".");
        }

        List<String> shortestPath = path.getVertexList();

        return new PathSearchResponse(calculateDuration(path.getEdgeList()), calculateDistance(path.getEdgeList()), shortestPath);
    }

    private WeightedMultigraph<String, PathWeightedEdge> getPathWeightedMultiGraph(PathType type) {
        WeightedMultigraph<String, PathWeightedEdge> pathGraph = new WeightedMultigraph<>(PathWeightedEdge.class);

        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();

        Map<Long, Station> stationMap = new HashMap<>(); //key = stationId, value = Station
        for (Station station : stations) {
            stationMap.put(station.getId(), station);
        }

        stations.forEach(station -> pathGraph.addVertex(station.getName()));

        lines.forEach(line -> {
            line.getLineStations().stream()
                    .filter(lineStation -> !Objects.isNull(lineStation.getPreStationId()))
                    .forEach(lineStation -> setLineStationEdgeByType(pathGraph, stationMap, lineStation, type));
        });
        return pathGraph;
    }

    private void setLineStationEdgeByType(WeightedMultigraph<String, PathWeightedEdge> pathGraph,
                                          Map<Long, Station> stationMap,
                                          LineStation lineStation,
                                          PathType type) {
        String preStationName = stationMap.get(lineStation.getPreStationId()).getName();
        String nextStationName = stationMap.get(lineStation.getStationId()).getName();
        PathWeightedEdge edge = pathGraph.addEdge(preStationName, nextStationName);
        edge.setDistance(lineStation.getDistance());
        edge.setDuration(lineStation.getDuration());
        if (PathType.DISTANCE.equals(type)) {
            pathGraph.setEdgeWeight(edge, lineStation.getDistance());
        }
        if (PathType.DURATION.equals(type)) {
            pathGraph.setEdgeWeight(edge, lineStation.getDuration());
        }
    }

    private int calculateDuration(List<PathWeightedEdge> edges) {
        return edges.stream()
                .map(edge -> edge.getDuration())
                .reduce(0, (a, b) -> a + b);
    }

    private int calculateDistance(List<PathWeightedEdge> edges) {
        return edges.stream()
                .map(edge -> edge.getDistance())
                .reduce(0, (a, b) -> a + b);
    }
}
