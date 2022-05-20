package wooteco.subway.domain;

import java.util.List;

public interface PathStrategy {
    List<Station> findPath(Station source, Station target);

    int calculateDistance(Station source, Station target);

    List<Long> findLineIds(Station source, Station target);
}
