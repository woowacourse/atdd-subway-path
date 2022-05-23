package wooteco.subway.domain;

import java.util.List;

public interface PathFindingStrategy {
    int getShortestDistance(Station source, Station target, Lines lines);

    List<Station> getShortestPath(Station source, Station target, Lines lines);

    List<Long> getLineIds(Station source, Station target, Lines lines);
}
