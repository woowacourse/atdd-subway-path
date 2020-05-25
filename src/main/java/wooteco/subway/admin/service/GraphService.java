package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GraphService {
    private Map<PathType, Graph> graphs = new ConcurrentHashMap<>();

    private CustomShortestPathAlgorithm shortestPathAlgorithm;

    public GraphService(CustomShortestPathAlgorithm shortestPathAlgorithm) {
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    public void initialize(Stations stations, LineStations lineStations) {
        Map<Long, Station> stationsCache = stations.convertMap();

        graphs.put(PathType.DISTANCE, Graph.of(stationsCache, lineStations, PathType.DISTANCE, shortestPathAlgorithm));
        graphs.put(PathType.DURATION, Graph.of(stationsCache, lineStations, PathType.DURATION, shortestPathAlgorithm));
    }

    public ShortestPath getShortestPath(Station source, Station target, PathType pathType) {
        return ShortestPath.of(graphs.get(pathType), source, target);
    }
}
