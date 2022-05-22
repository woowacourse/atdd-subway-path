package wooteco.subway.domain;

public interface Shortest {
    Path getShortestPath(final Station source, final Station target, final Fare fare, final int age);

    Long getExpensiveLineId(Station source, Station target);
}
