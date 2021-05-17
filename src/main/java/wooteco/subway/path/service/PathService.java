package wooteco.subway.path.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathServiceDto;
import wooteco.subway.path.exceptions.NotReachableException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.SameStationException;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse optimalPath(PathServiceDto pathServiceDto) {
        checkSameStation(pathServiceDto);
        Station sourceStation = stationService.findStationById(pathServiceDto.getSourceStationId());
        Station targetStation = stationService.findStationById(pathServiceDto.getTargetStationId());

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
        initializeGraph(graph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        return translateResponse(dijkstraShortestPath, sourceStation, targetStation);
    }

    private void initializeGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        initializeStationsOnGraph(graph);
        initializeSectionsOnGraph(graph);
    }

    private void initializeStationsOnGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stationService.findAllStationResponses()
            .stream()
            .map(element -> new Station(element.getId(), element.getName()))
            .collect(Collectors.toList())
            .forEach(element -> graph.addVertex(element));
    }

    private void initializeSectionsOnGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lineService.findLines().stream()
            .map(line -> line.getSections())
            .flatMap(sectionsOnLine -> sectionsOnLine.getSections().stream())
            .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                section.getDownStation()), section.getDistance()));
    }

    private PathResponse translateResponse(DijkstraShortestPath dijkstraShortestPath,
        Station sourceStation, Station targetStation) {
        List<StationResponse> stationResponses = stationConnection(dijkstraShortestPath,
            sourceStation, targetStation);
        Double weight = dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        return new PathResponse(stationResponses, weight);
    }

    private void checkSameStation(PathServiceDto pathServiceDto) {
        if (pathServiceDto.getSourceStationId() == pathServiceDto.getTargetStationId()) {
            throw new SameStationException(pathServiceDto.getSourceStationId());
        }
    }

    private List<StationResponse> stationConnection(DijkstraShortestPath dijkstraShortestPath,
        Station sourceStation, Station targetStation) {
        try {
            List<Station> shortestPath
                = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
            return shortestPath.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new NotReachableException(sourceStation.getId(), targetStation.getId());
        }
    }
}
