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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target, String type) {
        List<Line> allLines = lineRepository.findAll();

        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<LineStation> lineStations = allLines.stream()
                .map(Line::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Set<Long> allStationIds = lineStations.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toSet());

        // 모든 점 추가
        for (Long stationId : allStationIds) {
            graph.addVertex(stationId);
        }

        // 모든 간선 추가
        for (LineStation lineStation : lineStations) {
            if (lineStation.getPreStationId() != null) {
                graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()), lineStation.getDistance());
            }
        }

        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = allStations.stream()
                .filter(station -> station.getName().equals(source))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        Station targetStation = allStations.stream()
                .filter(station -> station.getName().equals(target))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);

        List<Long> shortestPathIds
                = dijkstraShortestPath.getPath(sourceStation.getId(), targetStation.getId()).getVertexList();

        List<Station> shortestPath = new ArrayList<>();
        Long distance = 0L;
        Long duration = 0L;

        Long preStationId = sourceStation.getId();

        for (Long stationId : shortestPathIds) {
            shortestPath.add(findStationBy(stationId, allStations));
            LineStation lineStation = findLineStation(preStationId, stationId, lineStations);
            distance += lineStation.getDistance();
            duration += lineStation.getDuration();
            preStationId = stationId;
        }
        return new PathResponse(StationResponse.listOf(shortestPath), distance, duration);
    }

    public Station findStationBy(Long id, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.is(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Station findStationBy(String name, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.is(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public LineStation findLineStation(Long preStationId, Long stationId, List<LineStation> lineStations) {
        for (LineStation lineStation : lineStations) {
            if (lineStation.is(preStationId, stationId)) {
                return lineStation;
            }
            if (lineStation.is(stationId, preStationId)) {
                return lineStation;
            }
        }
        return LineStation.empty();
    }
}
