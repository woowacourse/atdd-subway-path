package wooteco.subway.domain;

import java.util.List;

public class Path {

    private final int shortestDistance;
    private final List<Long> shortestPath;

    private Path(int shortestDistance, List<Long> shortestPath) {
        this.shortestDistance = shortestDistance;
        this.shortestPath = shortestPath;
    }

    public static Path of(PathFactory pathFactory, Long source, Long target) {
        validateSameStations(source, target);
        return new Path(pathFactory.findShortestDistance(source, target),
                pathFactory.findShortestPath(source, target));
    }

    private static void validateSameStations(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }
}
