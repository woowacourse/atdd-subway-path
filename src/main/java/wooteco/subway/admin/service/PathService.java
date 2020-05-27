package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.strategy.AlgorithmStrategy;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final AlgorithmStrategy algorithmStrategy;

    public PathService(LineRepository lineRepository, StationRepository stationRepository,
        AlgorithmStrategy algorithmStrategy) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.algorithmStrategy = algorithmStrategy;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        final Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, station -> station));
        final List<Line> lines = lineRepository.findAll();

        final GraphPath<Station, Edge> path = algorithmStrategy.getPath(stations, lines,
            pathRequest);

        List<Station> shortestPath = path.getVertexList();
        List<Edge> edgeList = path.getEdgeList();

        final int distance = edgeList.stream().mapToInt(Edge::getDistance).sum();
        final int duration = edgeList.stream().mapToInt(Edge::getDuration).sum();

        return new PathResponse(shortestPath, distance, duration);
    }
}
