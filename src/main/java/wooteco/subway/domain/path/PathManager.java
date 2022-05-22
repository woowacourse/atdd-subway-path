package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import wooteco.subway.domain.station.Station;

public class PathManager {

    private static final int START_STATION_DISTANCE = 0;
    private static final int START_STATION_FARE = 0;
    private static final int INFINITE_DISTANCE = Integer.MAX_VALUE;
    private static final String NOT_REGISTERED_STATION_EXCEPTION = "노선에 등록되지 않은 역을 입력하였습니다.";
    private static final String PATH_NOT_CONNECTED_EXCEPTION = "해당 역으로 이동하는 경로는 존재하지 않습니다.";
    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";

    private final Map<Station, List<PathElement>> pathMap;
    private final Map<Station, Integer> distanceResults;
    private final Map<Station, Integer> fareResults;
    private final Map<Station, Station> routeMap;
    private final PriorityQueue<PathElement> queue;

    private PathManager(Map<Station, List<PathElement>> pathMap) {
        this.pathMap = pathMap;
        distanceResults = new HashMap<>();
        fareResults = new HashMap<>();
        routeMap = new HashMap<>();
        queue = new PriorityQueue<>();
    }

    public static PathManager of(Map<Station, List<PathElement>> pathMap) {
        return new PathManager(pathMap);
    }

    public Path calculateOptimalPath(Station startStation, Station endStation) {
        validateRegisteredStations(startStation, endStation);
        validateNonSelfLoop(startStation, endStation);
        initializeDistanceResultsMap(startStation);
        queue.add(new PathElement(startStation, START_STATION_DISTANCE, START_STATION_FARE));

        while (!queue.isEmpty()) {
            PathElement current = queue.poll();
            Station nowStation = current.getStation();
            int nowDistance = current.getDistance();
            int nowFare = current.getExtraFare();
            if (distanceResults.get(nowStation) >= nowDistance) {
                updateNextShortestPath(nowStation, nowDistance, nowFare);
            }
        }
        validatePathConnection(endStation, distanceResults);
        return new Path(distanceResults.get(endStation), backTrackPath(startStation, endStation), fareResults.get(endStation));
    }

    private void updateNextShortestPath(Station nowStation, int nowDistance, int nowFare) {
        for (PathElement next : pathMap.get(nowStation)) {
            Station nextStation = next.getStation();
            int nextDistance = next.getDistance();
            int connectedDistance = nowDistance + nextDistance;
            int nextFare = Math.max(nowFare, next.getExtraFare());
            if (distanceResults.get(nextStation) > connectedDistance) {
                distanceResults.put(nextStation, connectedDistance);
                fareResults.put(nextStation, nextFare);
                queue.add(new PathElement(nextStation, connectedDistance, nextFare));
                routeMap.put(nextStation, nowStation);
            }
        }
    }

    private void validateRegisteredStations(Station startStation, Station endStation) {
        if (!pathMap.containsKey(startStation) || !pathMap.containsKey(endStation)) {
            throw new IllegalArgumentException(NOT_REGISTERED_STATION_EXCEPTION);
        }
    }

    private void validateNonSelfLoop(Station startStation, Station endStation) {
        if (startStation.equals(endStation)){
            throw new IllegalArgumentException(SELF_LOOP_EXCEPTION);
        }
    }

    private void initializeDistanceResultsMap(Station startStation) {
        for (Station station : pathMap.keySet()) {
            distanceResults.put(station, INFINITE_DISTANCE);
            fareResults.put(station, START_STATION_FARE);
        }
        distanceResults.put(startStation, START_STATION_DISTANCE);
        fareResults.put(startStation, START_STATION_FARE);
    }

    private void validatePathConnection(Station endStation, Map<Station, Integer> distanceResults) {
        if (distanceResults.get(endStation) == INFINITE_DISTANCE) {
            throw new IllegalArgumentException(PATH_NOT_CONNECTED_EXCEPTION);
        }
    }

    private List<Station> backTrackPath(Station startStation, Station endStation) {
        Station current = endStation;
        List<Station> optimalPath = new ArrayList<>();
        while (!current.equals(startStation)) {
            optimalPath.add(current);
            current = routeMap.get(current);
        }
        optimalPath.add(current);
        Collections.reverse(optimalPath);
        return optimalPath;
    }
}
