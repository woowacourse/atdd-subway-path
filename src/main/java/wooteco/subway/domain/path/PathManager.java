package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class PathManager {

    private static final String NOT_REGISTERED_STATION_EXCEPTION = "노선에 등록되지 않은 역을 입력하였습니다.";
    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";

    private final Map<Station, List<PathElement>> pathMap;

    private PathManager(Map<Station, List<PathElement>> pathMap) {
        this.pathMap = pathMap;
    }

    public static PathManager of(List<Section> sections) {
        return new PathManager(toPathMap(sections));
    }

    private static Map<Station, List<PathElement>> toPathMap(List<Section> sections) {
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

    public Path calculateOptimalPath(Station startStation, Station endStation) {
        validateRegisteredStations(startStation, endStation);
        validateNonSelfLoop(startStation, endStation);
        PathManager2 manager = PathManager2.of(pathMap, startStation);

        while (!manager.isEmptyQueue()) {
            updatePathManager(manager);
        }
        return manager.toConnectedPath(endStation);
    }

    private void validateRegisteredStations(Station startStation, Station endStation) {
        if (!pathMap.containsKey(startStation) || !pathMap.containsKey(endStation)) {
            throw new IllegalArgumentException(NOT_REGISTERED_STATION_EXCEPTION);
        }
    }

    private void validateNonSelfLoop(Station startStation, Station endStation) {
        if (startStation.equals(endStation)) {
            throw new IllegalArgumentException(SELF_LOOP_EXCEPTION);
        }
    }

    private void updatePathManager(PathManager2 manager) {
        PathElement current = manager.poll();
        if (!manager.isCloserThanCurrentDistance(current)) {
            manager.updateShortestPath(current, pathMap.get(current.getStation()));
        }
    }
}
