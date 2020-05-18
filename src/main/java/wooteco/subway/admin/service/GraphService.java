package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class GraphService {
    private Map<PathType, Graph> graphs = new EnumMap<>(PathType.class);

    public void initialize(Stations stations, Lines lines) {
        Map<Long, Station> stationsCache = stations.convertMap();
        List<LineStation> edges = lines.getEdges();

        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            graphs.put(PathType.DISTANCE, Graph.of(stationsCache, edges, PathType.DISTANCE));
            graphs.put(PathType.DURATION, Graph.of(stationsCache, edges, PathType.DURATION));
        } finally {
            lock.unlock();
        }
    }

    public ShortestPath getShortestPath(Station source, Station target, PathType pathType) {
        return ShortestPath.of(graphs.get(pathType), source, target);
    }
}
