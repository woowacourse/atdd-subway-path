package wooteco.subway.domain.path;

import wooteco.subway.domain.Station;

public interface ShortestPath {
    Path getPath(final Station source, final Station target, final int fare, int age);

    Long getExpensiveLineId(Station source, Station target);
}
