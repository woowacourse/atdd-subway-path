package wooteco.subway.admin.service;

import java.util.List;
import java.util.function.ToIntFunction;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathSearchType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.SubwayEdge;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.DisconnectedPathException;
import wooteco.subway.admin.exception.SameSourceAndDestinationException;
import wooteco.subway.admin.repository.LineRepository;

@Transactional
@Service
public class JGraphAlgorithmService implements PathAlgorithmService {
    private static final int EMPTY_PATH_SIZE = 0;

    private final LineRepository lineRepository;

    public JGraphAlgorithmService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public ShortestPathResponse findShortestPath(PathRequest pathRequest, Stations stations) {
        Station source = stations.findByName(pathRequest.getSource());
        Station target = stations.findByName(pathRequest.getTarget());
        PathSearchType searchType = PathSearchType.of(pathRequest.getType());
        WeightedMultigraph<Station, SubwayEdge> graph = initGraph(searchType, stations);
        return getShortestPathResponse(graph, source, target);
    }

    private WeightedMultigraph<Station, SubwayEdge> initGraph(PathSearchType searchType, Stations stations) {
        WeightedMultigraph<Station, SubwayEdge> graph = new WeightedMultigraph<>(SubwayEdge.class);
        initGraphVertex(graph, stations);
        initGraphEdges(graph, searchType, stations);
        return graph;
    }

    private void initGraphVertex(WeightedMultigraph<Station, SubwayEdge> graph, Stations stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void initGraphEdges(WeightedMultigraph<Station, SubwayEdge> graph, PathSearchType type, Stations stations) {
        lineRepository.findAll().stream()
            .flatMap(line -> line.getStations().getStations().stream())
            .filter(LineStation::isNotFirstLineStation)
            .forEach(lineStation -> addOneEdgeAndSetWeight(graph, type, stations, lineStation));
    }

    private void addOneEdgeAndSetWeight(WeightedMultigraph<Station, SubwayEdge> graph, PathSearchType type,
        Stations stations, LineStation lineStation) {
        Station preStation = stations.findById(lineStation.getPreStationId());
        Station station = stations.findById(lineStation.getStationId());
        SubwayEdge edge = SubwayEdge.from(lineStation);
        graph.addEdge(preStation, station, edge);
        graph.setEdgeWeight(edge, type.getEdgeWeight(lineStation));
    }

    private ShortestPathResponse getShortestPathResponse(Graph<Station, SubwayEdge> graph, Station source,
        Station target) {
        GraphPath<Station, SubwayEdge> calculatedPath = calculateShortestPath(graph, source, target);
        List<Station> paths = calculatedPath.getVertexList();
        int totalDuration = calculateSum(calculatedPath, SubwayEdge::getDuration);
        int totalDistance = calculateSum(calculatedPath, SubwayEdge::getDistance);
        return new ShortestPathResponse(StationResponse.listOf(paths), totalDistance, totalDuration);
    }

    private GraphPath<Station, SubwayEdge> calculateShortestPath(Graph<Station, SubwayEdge> graph, Station source,
        Station target) {
        DijkstraShortestPath<Station, SubwayEdge> dijkstraPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, SubwayEdge> calculatedPath = dijkstraPath.getPath(source, target);
        validateCreatedPath(calculatedPath);
        return calculatedPath;
    }

    private void validateCreatedPath(GraphPath<Station, SubwayEdge> path) {
        if (path == null) {
            throw new DisconnectedPathException();
        }

        if (path.getLength() == EMPTY_PATH_SIZE) {
            throw new SameSourceAndDestinationException();
        }
    }

    private int calculateSum(GraphPath<Station, SubwayEdge> path, ToIntFunction<SubwayEdge> valueMapper) {
        return path.getEdgeList().stream().mapToInt(valueMapper).sum();
    }
}
