package wooteco.subway.admin.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target, String type) {

        Lines allLines = new Lines(lineRepository.findAll());

        LineStations lineStations = new LineStations(allLines.getAllLineStation());

        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = findStationByName(source, allStations);
        Station targetStation = findStationByName(target, allStations);

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = initGraph(lineStations);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Long> shortestPathIds = dijkstraShortestPath.getPath(sourceStation.getId(), targetStation.getId()).getVertexList();

        List<Station> shortestPath = new ArrayList<>();
        int distance = 0;
        int duration = 0;
        Long preStationId = sourceStation.getId();

        for (Long stationId : shortestPathIds) {
            shortestPath.add(findStationById(stationId, allStations));
            LineStation lineStation = lineStations.findLineStation(preStationId, stationId);
            distance += lineStation.getDistance();
            duration += lineStation.getDuration();
            preStationId = stationId;
        }

        return new PathResponse(StationResponse.listOf(shortestPath), distance, duration);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> initGraph(LineStations lineStations) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        Set<Long> allStationIds = lineStations.getAllLineStationId();

        for (Long stationId : allStationIds) {
            graph.addVertex(stationId);
        }

        for (LineStation lineStation : lineStations.getLineStations()) {
            if (lineStation.getPreStationId() != null) {
                graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()), lineStation.getDistance());
            }
        }
        return graph;
    }

    public Station findStationById(Long id, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.is(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Station findStationByName(String name, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.is(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
