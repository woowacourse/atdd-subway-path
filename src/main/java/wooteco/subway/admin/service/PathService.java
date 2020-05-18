package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.WrongPathException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

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

    public PathResponse calculatePath(PathRequest request) {
        Stations stations = new Stations(stationRepository.findAll());
        Lines lines = new Lines(lineRepository.findAll());
        LineStations lineStations = new LineStations(lines.getLineStations());

        Long sourceId = stations.findStationIdByName(request.getSource());
        Long targetId = stations.findStationIdByName(request.getTarget());

        if (sourceId.equals(targetId)) {
            throw new WrongPathException();
        }

        List<Long> shortestPath = createShortestPath(lines, sourceId, targetId, request.getType());
        return createPathResponse(shortestPath, stations, lineStations);
    }

    private PathResponse createPathResponse(List<Long> path, Stations stations, LineStations lineStations) {
        Stations pathStations = path.stream()
                .map(stations::findStationById)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));
        LineStations pathLineStations = lineStations.findLineStationsByIds(path);

        int duration = pathLineStations.getWeightBy(PathType.DURATION);
        int distance = pathLineStations.getWeightBy(PathType.DISTANCE);
        return new PathResponse(pathStations, distance, duration);
    }

    private List<Long> createShortestPath(Lines lines, Long source, Long target, PathType type) {
        try {
            return createDijkstraShortestPathByLines(lines, type).getPath(source, target).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new WrongPathException();
        }
    }

    private DijkstraShortestPath<Long, DefaultWeightedEdge> createDijkstraShortestPathByLines(Lines lines, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Long id : lines.getLineStationsId()) {
            graph.addVertex(id);
        }
        for (LineStation lineStation : lines.getLineStations()) {
            if (Objects.nonNull(lineStation.getPreStationId())) {
                graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()), type.getWeight(lineStation));
            }
        }
        return new DijkstraShortestPath<>(graph);
    }
}
