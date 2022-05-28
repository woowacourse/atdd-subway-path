package wooteco.subway.infrastructure;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Station;

public class PathAdapter {
    private final PathFinder pathFinder;

    public PathAdapter(final PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public Path getShortestPath(final Station source, final Station target, final int fare, final int age) {
        return pathFinder.getPath(source, target, fare, age);
    }

    public Long getExpensiveLineId(final Station source, final Station target) {
        return pathFinder.getExpensiveLineId(source, target);
    }
}
