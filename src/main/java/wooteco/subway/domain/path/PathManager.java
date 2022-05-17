package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class PathManager {

    private static final int START_STATION_DISTANCE = 0;
    private static final int INFINITE_DISTANCE = Integer.MAX_VALUE;
    private static final String NOT_REGISTERED_STATION_EXCEPTION = "노선에 등록되지 않은 역을 입력하였습니다.";
    private static final String PATH_NOT_CONNECTED_EXCEPTION = "해당 역으로 이동하는 경로는 존재하지 않습니다.";
    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";

    private final Map<Station, List<PathElement>> pathMap;

    private PathManager(Map<Station, List<PathElement>> pathMap) {
        this.pathMap = pathMap;
    }

    public static PathManager of(List<Section> sections) {
        return new PathManager(toAdjacentPath(sections));
    }

    private static Map<Station, List<PathElement>> toAdjacentPath(List<Section> sections) {
        Map<Station, List<PathElement>> adjacentPaths = new HashMap<>();
        for (Section section : sections) {
            adjacentPaths.put(section.getUpStation(), new ArrayList<>());
            adjacentPaths.put(section.getDownStation(), new ArrayList<>());
        }
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            adjacentPaths.get(upStation).add(new PathElement(downStation, distance));
            adjacentPaths.get(downStation).add(new PathElement(upStation, distance));
        }
        return adjacentPaths;
    }

    public PathResult calculateOptimalPath(Station startStation, Station endStation) {
        validateRegisteredStations(startStation, endStation);
        validateNonSelfLoop(startStation, endStation);
        Map<Station, Integer> distanceResults = getInitialDistanceResultsMap(startStation);
        Map<Station, Station> routeMap = getPreviousRouteMap();
        PriorityQueue<PathElement> queue = new PriorityQueue<>();
        queue.add(new PathElement(startStation, START_STATION_DISTANCE));

        while (!queue.isEmpty()) {
            PathElement current = queue.poll();
            if (distanceResults.get(current.station) >= current.distance) {
                updateShortestPath(distanceResults, routeMap, queue, current);
            }
        }
        validatePathConnection(endStation, distanceResults);
        return new PathResult(distanceResults.get(endStation), toSortedPath(endStation, routeMap));
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

    private Map<Station, Integer> getInitialDistanceResultsMap(Station startStation) {
        Map<Station, Integer> distanceResults = new HashMap<>();
        for (Station station : pathMap.keySet()) {
            distanceResults.put(station, INFINITE_DISTANCE);
        }
        distanceResults.put(startStation, START_STATION_DISTANCE);
        return distanceResults;
    }

    private Map<Station, Station> getPreviousRouteMap() {
        Map<Station, Station> prevRoute = new HashMap<>();
        for (Station station : pathMap.keySet()) {
            prevRoute.put(station, null);
        }
        return prevRoute;
    }

    private void updateShortestPath(Map<Station, Integer> distanceResults,
                                    Map<Station, Station> routeMap,
                                    PriorityQueue<PathElement> queue,
                                    PathElement current) {
        for (PathElement next : pathMap.get(current.station)) {
            Station nextStation = next.station;
            int connectedDistance = current.distance + next.distance;
            if (distanceResults.get(nextStation) > connectedDistance) {
                distanceResults.put(nextStation, connectedDistance);
                queue.add(new PathElement(nextStation, connectedDistance));
                routeMap.put(nextStation, current.station);
            }
        }
    }

    private void validatePathConnection(Station endStation, Map<Station, Integer> distanceResults) {
        if (distanceResults.get(endStation) == INFINITE_DISTANCE) {
            throw new IllegalArgumentException(PATH_NOT_CONNECTED_EXCEPTION);
        }
    }

    private List<Station> toSortedPath(Station endStation, Map<Station, Station> routeMap) {
        Station current = endStation;
        List<Station> optimalPath = new ArrayList<>();
        while (current != null) {
            optimalPath.add(current);
            current = routeMap.get(current);
        }
        Collections.reverse(optimalPath);
        return optimalPath;
    }

    private static class PathElement implements Comparable<PathElement> {

        private final Station station;
        private final int distance;

        private PathElement(Station station, int distance) {
            this.station = station;
            this.distance = distance;
        }

        @Override
        public int compareTo(PathElement o) {
            return Integer.compare(this.distance, o.distance);
        }
    }
}
