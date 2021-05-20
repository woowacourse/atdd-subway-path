package wooteco.subway.path.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.exception.NotReachableException;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;
import wooteco.subway.station.exception.SameStationException;

@Service
public class PathService {

    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse optimalPath(PathRequest pathRequest) {
        checkSameStation(pathRequest);
        Station sourceStation = stationDao.findById(pathRequest.getSourceStationId());
        Station targetStation = stationDao.findById(pathRequest.getTargetStationId());

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initializeGraph(graph);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath
            = new DijkstraShortestPath(graph);

        return translateResponse(dijkstraShortestPath, sourceStation, targetStation);
    }

    private void initializeGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        initializeStationsOnGraph(graph);
        initializeSectionsOnGraph(graph);
    }

    private void initializeStationsOnGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stationDao.findAll()
            .stream()
            .collect(Collectors.toList())
            .forEach(element -> graph.addVertex(element));
    }

    private void initializeSectionsOnGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lineDao.findAll().stream()
            .map(line -> line.getSections())
            .flatMap(sectionsOnLine -> sectionsOnLine.getSections().stream())
            .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                section.getDownStation()), section.getDistance()));
    }

    private PathResponse translateResponse(DijkstraShortestPath<Station, DefaultWeightedEdge>
        dijkstraShortestPath, Station sourceStation, Station targetStation) {
        List<StationResponse> stationResponses = stationConnection(dijkstraShortestPath,
            sourceStation, targetStation);
        Double weight = dijkstraShortestPath.getPathWeight(sourceStation, targetStation);
        return new PathResponse(stationResponses, weight);
    }

    private void checkSameStation(PathRequest pathRequest) {
        if (pathRequest.getSourceStationId() == pathRequest.getTargetStationId()) {
            throw new SameStationException(pathRequest.getSourceStationId());
        }
    }

    private List<StationResponse> stationConnection(DijkstraShortestPath<Station,
        DefaultWeightedEdge> dijkstraShortestPath, Station sourceStation, Station targetStation) {
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
