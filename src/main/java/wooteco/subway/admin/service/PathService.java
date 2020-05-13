package wooteco.subway.admin.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPathByDistance(Long startId, Long endId) {
        final List<Station> stations = stationRepository.findAll();
        final List<Line> lines = lineRepository.findAll();

        final Station startStation = findStation(stations, startId);
        final Station endStation = findStation(stations, endId);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (final Station station : stations) {
            graph.addVertex(station);
        }

        for (final Line line : lines) {
            final Set<LineStation> lineStations = line.getStations();

            for (final LineStation lineStation : lineStations) {
                if (Objects.isNull(lineStation.getPreStationId())) {
                    continue;
                }

                Station preStation = findStation(stations, lineStation.getPreStationId());
                Station currentStation = findStation(stations, lineStation.getStationId());

                graph.setEdgeWeight(graph.addEdge(preStation, currentStation), lineStation.getDistance());
            }
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<Station> shortestPath = dijkstraShortestPath.getPath(startStation, endStation).getVertexList();
        double distance = dijkstraShortestPath.getPath(startStation, endStation).getWeight();
        return new PathResponse(shortestPath, (int) distance, 40);
    }

    private Station findStation(final List<Station> stations, final Long stationId) {
        return stations.stream()
            .filter(station -> Objects.equals(station.getId(), stationId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
