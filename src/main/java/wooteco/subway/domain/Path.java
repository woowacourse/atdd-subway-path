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
        return new Path(pathFactory.findShortestDistance(source, target),
                pathFactory.findShortestPath(source, target));
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

    public List<Long> getShortestPath() {
        return shortestPath;
    }
}
