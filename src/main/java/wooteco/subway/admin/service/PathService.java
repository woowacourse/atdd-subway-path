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
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.StationNotFoundException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse getPath(final String source, final String target) {
        WeightedMultigraph<Long, LineStation> shortestDistanceGraph
            = new WeightedMultigraph<>(LineStation.class);

        Station sourceStation = stationRepository.findByName(source)
            .orElseThrow(() -> new StationNotFoundException(source));
        Station targetStation = stationRepository.findByName(target)
            .orElseThrow(() -> new StationNotFoundException(target));

        stationRepository.findAll()
            .stream()
            .map(Station::getId)
            .forEach(shortestDistanceGraph::addVertex);

        lineRepository.findAll()
            .stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .filter(station -> !Objects.isNull(station.getPreStationId()))
            .forEach(station ->
                shortestDistanceGraph.setEdgeWeight(
                    shortestDistanceGraph.addEdge(station.getPreStationId(), station.getStationId()),
                    station.getDistance())
            );
        DijkstraShortestPath<Long, LineStation> dijkstraShortestPath = new DijkstraShortestPath<>(
            shortestDistanceGraph);
        GraphPath<Long, LineStation> shortestPath = dijkstraShortestPath.getPath(sourceStation.getId(),
            targetStation.getId());

        Integer integer = shortestPath.getEdgeList()
            .stream()
            .map(LineStation::getDuration)
            .reduce(Integer::sum)
            .orElse(0);

        DijkstraShortestPath.findPathBetween(shortestPath.getGraph(), 1L, 3L);
        List<StationResponse> stations = stationRepository.findAllById(shortestPath.getVertexList())
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        return new PathResponse(stations, shortestPath.getWeight(), integer);
    }
}
