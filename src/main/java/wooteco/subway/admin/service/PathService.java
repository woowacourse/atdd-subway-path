package wooteco.subway.admin.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.PathEdge;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.InvalidSubwayPathException;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse getPath(final String sourceName, final String targetName, final PathType pathType) {
        WeightedMultigraph<Long, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);

        Station source = stationRepository.findByName(sourceName)
            .orElseThrow(() -> new StationNotFoundException(sourceName));
        Station target = stationRepository.findByName(targetName)
            .orElseThrow(() -> new StationNotFoundException(targetName));

        if (source.is(target)) {
            throw new InvalidSubwayPathException("출발역과 도착역은 같을 수 없습니다.");
        }

        stationRepository.findAll()
            .stream()
            .map(Station::getId)
            .forEach(graph::addVertex);

        lineRepository.findAll()
            .stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .map(lineStation -> new PathEdge(lineStation, pathType))
            .filter(PathEdge::isNotFirst)
            .forEach(edge -> {
                    graph.addEdge(edge.getPreStationId(), edge.getStationId(), edge);
                    graph.setEdgeWeight(edge, edge.getWeight());
                }
            );

        GraphPath<Long, PathEdge> path = DijkstraShortestPath.findPathBetween(
            graph,
            source.getId(),
            target.getId()
        );

        if (Objects.isNull(path)) {
            throw new InvalidSubwayPathException("존재하지 않는 경로입니다.");
        }

        List<StationResponse> stations = stationRepository
            .findAllById(path.getVertexList())
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return new PathResponse(stations, getDistance(path), getDuration(path));
    }

    private Integer getDistance(final GraphPath<Long, PathEdge> path) {
        return path.getEdgeList()
            .stream()
            .map(PathEdge::getDistance)
            .reduce(Integer::sum)
            .orElse(0);
    }

    private Integer getDuration(final GraphPath<Long, PathEdge> path) {
        return path.getEdgeList()
            .stream()
            .map(PathEdge::getDuration)
            .reduce(Integer::sum)
            .orElse(0);
    }
}
