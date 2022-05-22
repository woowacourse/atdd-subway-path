package wooteco.subway.domain;

public interface ShortestPath {
    Path getPath(final Station source, final Station target, final Fare fare, int age);

    Long getExpensiveLineId(Station source, Station target);
}
