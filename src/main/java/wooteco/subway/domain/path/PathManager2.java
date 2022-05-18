package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import wooteco.subway.domain.station.Station;

// TODO: needs more refactoring
public class PathManager2 {

    private static final String PATH_NOT_CONNECTED_EXCEPTION = "해당 역으로 이동하는 경로는 존재하지 않습니다.";
    private static final int START_STATION_DISTANCE = 0;
    private static final int INFINITE_DISTANCE = Integer.MAX_VALUE;

    private final PriorityQueue<PathElement> queue;
    private final Map<Station, Integer> distanceMap;
    private final Map<Station, Station> routeMap;

    private PathManager2(PriorityQueue<PathElement> queue,
                         Map<Station, Integer> distanceResults,
                         Map<Station, Station> routeMap) {
        this.queue = queue;
        this.distanceMap = distanceResults;
        this.routeMap = routeMap;
    }

    public static PathManager2 of(Map<Station, List<PathElement>> pathMap,
                                  Station startStation) {
        PriorityQueue<PathElement> queue = initQueue(startStation);
        Map<Station, Integer> distanceResultsMap = initDistanceResultsMap(pathMap, startStation);
        Map<Station, Station> routeMap = new HashMap<>();
        return new PathManager2(queue, distanceResultsMap, routeMap);
    }

    private static PriorityQueue<PathElement> initQueue(Station startStation) {
        PriorityQueue<PathElement> queue = new PriorityQueue<>();
        queue.add(new PathElement(startStation, START_STATION_DISTANCE));
        return queue;
    }

    private static Map<Station, Integer> initDistanceResultsMap(Map<Station, List<PathElement>> pathMap,
                                                                Station startStation) {
        Map<Station, Integer> distanceResults = new HashMap<>();
        for (Station station : pathMap.keySet()) {
            distanceResults.put(station, INFINITE_DISTANCE);
        }
        distanceResults.put(startStation, START_STATION_DISTANCE);
        return distanceResults;
    }

    public boolean isEmptyQueue() {
        return queue.isEmpty();
    }

    public PathElement poll() {
        return queue.poll();
    }

    public boolean isCloserThanCurrentDistance(PathElement current) {
        return distanceMap.get(current.getStation()) < current.getDistance();
    }

    public void updateShortestPath(PathElement current,
                                   List<PathElement> pathElements) {
        for (PathElement next : pathElements) {
            Station nextStation = next.getStation();
            int connectedDistance = current.toConnectedDistance(next);
            if (distanceMap.get(nextStation) > connectedDistance) {
                queue.add(new PathElement(nextStation, connectedDistance));
                distanceMap.put(nextStation, connectedDistance);
                routeMap.put(nextStation, current.getStation());
            }
        }
    }

    public Path toConnectedPath(Station endStation) {
        validatePathConnection(endStation);
        int totalDistance = distanceMap.get(endStation);
        return Path.of(toConnectedStations(endStation), totalDistance);
    }

    private void validatePathConnection(Station endStation) {
        if (distanceMap.get(endStation) == INFINITE_DISTANCE) {
            throw new IllegalArgumentException(PATH_NOT_CONNECTED_EXCEPTION);
        }
    }

    private List<Station> toConnectedStations(Station endStation) {
        List<Station> optimalPath = new ArrayList<>();
        Station current = endStation;
        while (current != null) {
            optimalPath.add(0, current);
            current = routeMap.get(current);
        }
        return optimalPath;
    }
}
