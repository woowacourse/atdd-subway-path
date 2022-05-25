package wooteco.subway.domain.path;

import wooteco.subway.domain.Station;

public class PathAdapter {
    private final ShortestPath shortestPath;

    public PathAdapter(final ShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Path getShortestPath(final Station source, final Station target, final int fare, final int age) {
        return shortestPath.getPath(source, target, fare, age);
    }

    public Long getExpensiveLineId(final Station source, final Station target) {
        return shortestPath.getExpensiveLineId(source, target);
    }
}
