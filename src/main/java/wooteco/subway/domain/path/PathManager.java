package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class PathManager {

    private static final String NOT_REGISTERED_STATION_EXCEPTION = "노선에 등록되지 않은 역을 입력하였습니다.";
    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";

    private final Map<Station, List<TargetElement>> targetElementMap;

    private PathManager(Map<Station, List<TargetElement>> targetElementMap) {
        this.targetElementMap = targetElementMap;
    }

    public static PathManager of(List<Section> sections) {
        return new PathManager(toPathMap(sections));
    }

    private static Map<Station, List<TargetElement>> toPathMap(List<Section> sections) {
        Map<Station, List<TargetElement>> adjacentPaths = new HashMap<>();
        for (Section section : sections) {
            adjacentPaths.put(section.getUpStation(), new ArrayList<>());
            adjacentPaths.put(section.getDownStation(), new ArrayList<>());
        }
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            adjacentPaths.get(upStation).add(new TargetElement(downStation, distance));
            adjacentPaths.get(downStation).add(new TargetElement(upStation, distance));
        }
        return adjacentPaths;
    }

    public Path calculateOptimalPath(Station startStation, Station endStation) {
        validateRegisteredStations(startStation, endStation);
        validateNonSelfLoop(startStation, endStation);
        DijkstraState dijkstraState = DijkstraState.of(targetElementMap, startStation);

        while (!dijkstraState.isEmptyQueue()) {
            dijkstraState.updateTargetElementMap(targetElementMap);
        }
        return dijkstraState.toConnectedPath(endStation);
    }

    private void validateRegisteredStations(Station startStation, Station endStation) {
        if (!targetElementMap.containsKey(startStation) || !targetElementMap.containsKey(endStation)) {
            throw new IllegalArgumentException(NOT_REGISTERED_STATION_EXCEPTION);
        }
    }

    private void validateNonSelfLoop(Station startStation, Station endStation) {
        if (startStation.equals(endStation)) {
            throw new IllegalArgumentException(SELF_LOOP_EXCEPTION);
        }
    }

    private static class TargetElement implements Comparable<TargetElement> {

        final Station station;
        final int distance;

        TargetElement(Station station, int distance) {
            this.station = station;
            this.distance = distance;
        }

        @Override
        public int compareTo(TargetElement o) {
            return Integer.compare(this.distance, o.distance);
        }
    }

    private static class DijkstraState {

        static final String PATH_NOT_CONNECTED_EXCEPTION = "해당 역으로 이동하는 경로는 존재하지 않습니다.";
        static final int START_STATION_DISTANCE = 0;
        static final int INFINITE_DISTANCE = Integer.MAX_VALUE;

        final PriorityQueue<TargetElement> queue;
        final Map<Station, Integer> distanceResultsMap;
        final Map<Station, Station> routeMap;

        DijkstraState(PriorityQueue<TargetElement> queue,
                      Map<Station, Integer> distanceResultsMap,
                      Map<Station, Station> routeMap) {
            this.queue = queue;
            this.distanceResultsMap = distanceResultsMap;
            this.routeMap = routeMap;
        }

        static DijkstraState of(Map<Station, List<TargetElement>> targetElementMap,
                                Station startStation) {
            PriorityQueue<TargetElement> queue = initQueue(startStation);
            Map<Station, Integer> distanceResultsMap = initDistanceResultsMap(targetElementMap, startStation);
            Map<Station, Station> routeMap = new HashMap<>();
            return new DijkstraState(queue, distanceResultsMap, routeMap);
        }

        static PriorityQueue<TargetElement> initQueue(Station startStation) {
            PriorityQueue<TargetElement> queue = new PriorityQueue<>();
            queue.add(new TargetElement(startStation, START_STATION_DISTANCE));
            return queue;
        }

        static Map<Station, Integer> initDistanceResultsMap(Map<Station, List<TargetElement>> targetElementMap,
                                                            Station startStation) {
            Map<Station, Integer> distanceResults = new HashMap<>();
            for (Station station : targetElementMap.keySet()) {
                distanceResults.put(station, INFINITE_DISTANCE);
            }
            distanceResults.put(startStation, START_STATION_DISTANCE);
            return distanceResults;
        }

        boolean isEmptyQueue() {
            return queue.isEmpty();
        }

        void updateTargetElementMap(Map<Station, List<TargetElement>> targetElementMap) {
            TargetElement current = queue.poll();
            List<TargetElement> targetStations = targetElementMap.get(current.station);
            if (!isCloserThanCurrentDistance(current)) {
                updateShortestPath(current, targetStations);
            }
        }

        boolean isCloserThanCurrentDistance(TargetElement current) {
            return distanceResultsMap.get(current.station) < current.distance;
        }

        void updateShortestPath(TargetElement current,
                                List<TargetElement> targetStations) {
            for (TargetElement next : targetStations) {
                Station nextStation = next.station;
                int connectedDistance = current.distance + next.distance;
                if (distanceResultsMap.get(nextStation) > connectedDistance) {
                    queue.add(new TargetElement(nextStation, connectedDistance));
                    distanceResultsMap.put(nextStation, connectedDistance);
                    routeMap.put(nextStation, current.station);
                }
            }
        }

        Path toConnectedPath(Station endStation) {
            validatePathConnection(endStation);
            int totalDistance = distanceResultsMap.get(endStation);
            return Path.of(toConnectedStations(endStation), totalDistance);
        }

        void validatePathConnection(Station endStation) {
            if (distanceResultsMap.get(endStation) == INFINITE_DISTANCE) {
                throw new IllegalArgumentException(PATH_NOT_CONNECTED_EXCEPTION);
            }
        }

        List<Station> toConnectedStations(Station endStation) {
            List<Station> optimalPath = new ArrayList<>();
            Station current = endStation;
            while (current != null) {
                optimalPath.add(0, current);
                current = routeMap.get(current);
            }
            return optimalPath;
        }
    }
}
