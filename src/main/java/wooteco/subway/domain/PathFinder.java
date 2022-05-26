package wooteco.subway.domain;

public interface PathFinder {
    Path getPath(final Station source, final Station target, final int fare, int age);

    Long getExpensiveLineId(Station source, Station target);
}
