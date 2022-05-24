package wooteco.subway.domain.path;

import wooteco.subway.domain.Station;

public interface Shortest {
    Path getShortestPath(final Station source, final Station target, final int fare, final int age);

    Long getExpensiveLineId(Station source, Station target);
}
