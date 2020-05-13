package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private LineService lineService;

    public PathService(LineService lineService) {
        this.lineService = lineService;
    }

    public List<Station> retrieveShortestPath(String decodedSource, String decodedTarget, PathType pathType) {
        Station source = lineService.findStationWithName(decodedSource);
        Station target = lineService.findStationWithName(decodedTarget);

        Stations stations = Stations.of(lineService.findAllStations());
        Map<Long, Station> stationCache = stations.convertMap();

        Lines lines = Lines.of(lineService.showLines());

        List<LineStation> edges = lines.getEdges();

        Graph graph = Graph.of(stationCache, edges);

        return graph.findShortestPath(source, target, pathType);
    }

    public int retrieveDuration(String decodedSource, String decodedTarget, PathType pathType) {
        Station source = lineService.findStationWithName(decodedSource);
        Station target = lineService.findStationWithName(decodedTarget);

        Stations stations = Stations.of(lineService.findAllStations());
        Map<Long, Station> stationCache = stations.convertMap();

        Lines lines = Lines.of(lineService.showLines());

        List<LineStation> edges = lines.getEdges();

        Graph graph = Graph.of(stationCache, edges);

        return graph.getDuration(source, target, pathType);
    }

    public int retrieveDistance(String decodedSource, String decodedTarget, PathType pathType) {
        Station source = lineService.findStationWithName(decodedSource);
        Station target = lineService.findStationWithName(decodedTarget);

        Stations stations = Stations.of(lineService.findAllStations());
        Map<Long, Station> stationCache = stations.convertMap();

        Lines lines = Lines.of(lineService.showLines());

        List<LineStation> edges = lines.getEdges();

        Graph graph = Graph.of(stationCache, edges);

        return graph.getDistance(source, target, pathType);
    }
}
