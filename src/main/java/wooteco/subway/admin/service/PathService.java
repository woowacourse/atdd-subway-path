package wooteco.subway.admin.service;

import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.CanNotCreateGraphException;
import wooteco.subway.admin.exception.LineNotConnectedException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PathService {
    public static final String DISTANCE = "DISTANCE";
    public static final String DURATION = "DURATION";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target, String type) {
        Lines allLines = new Lines(lineRepository.findAll());
        LineStations lineStations = new LineStations(allLines.getAllLineStation());
        Stations stations = new Stations(stationRepository.findAll());
        Station sourceStation = stations.findStationByName(source);
        Station targetStation = stations.findStationByName(target);

        ShortestPathAlgorithm<Long, DefaultEdge> shortestPathAlgorithm = prepareAlgorithm(type, lineStations);
        List<Long> shortestPathIds = calculateshortestPathIDs(sourceStation, targetStation, shortestPathAlgorithm);

        List<Station> shortestPathStations = shortestPathIds.stream()
                .map(stations::findStationById)
                .collect(Collectors.toList());

        List<LineStation> shortestPathLineStations = calculateShortestPathLineStations(lineStations, sourceStation.getId(), shortestPathIds);

        int distance = getAccumulateValue(shortestPathLineStations, LineStation::getDistance);
        int duration = getAccumulateValue(shortestPathLineStations, LineStation::getDuration);

        return new PathResponse(StationResponse.listOf(shortestPathStations), distance, duration);
    }

    private List<LineStation> calculateShortestPathLineStations(LineStations lineStations, Long sourceStationId, List<Long> shortestPathIds) {
        Long preStationId = sourceStationId;

        List<LineStation> shortestPathLineStations = new ArrayList<>();
        for (Long stationId : shortestPathIds) {
            shortestPathLineStations.add(lineStations.findLineStation(preStationId, stationId));
            preStationId = stationId;
        }
        return shortestPathLineStations;
    }

    private ShortestPathAlgorithm<Long, DefaultEdge> prepareAlgorithm(String type, LineStations lineStations) {
        WeightedGraph<Long, DefaultEdge> graph;

        try {
            graph = initGraph(lineStations, type);
        } catch (IllegalArgumentException e) {
            throw new CanNotCreateGraphException();
        }

        return new DijkstraShortestPath<>(graph);
    }

    private List<Long> calculateshortestPathIDs(Station sourceStation, Station targetStation, ShortestPathAlgorithm<Long, DefaultEdge> shortestPathAlgorithm) {
        List<Long> shortestPathIds;
        try {
            shortestPathIds = shortestPathAlgorithm.getPath(sourceStation.getId(), targetStation.getId()).getVertexList();
        } catch (NullPointerException e) {
            throw new LineNotConnectedException();
        }
        return shortestPathIds;
    }

    private Integer getAccumulateValue(List<LineStation> shortestPathLineStations, Function<LineStation, Integer> strategy) {
        return shortestPathLineStations.stream()
                .map(strategy)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private WeightedGraph<Long, DefaultEdge> initGraph(LineStations lineStations, String type) {
        WeightedGraph<Long, DefaultEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        Set<Long> allStationIds = lineStations.getAllLineStationId();

        for (Long stationId : allStationIds) {
            graph.addVertex(stationId);
        }

        for (LineStation lineStation : lineStations.getLineStations()) {
            if (lineStation.getPreStationId() != null) {
                DefaultEdge edge = graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId());
                if (type.equals(DISTANCE)) {
                    graph.setEdgeWeight(edge, lineStation.getDistance());
                }
                if (type.equals(DURATION)) {
                    graph.setEdgeWeight(edge, lineStation.getDuration());
                }
            }
        }
        return graph;
    }
}
