package wooteco.subway.domain.path;

import wooteco.subway.domain.Station;

public interface PathCalculator {
    Path findShortestPath(final Station source, final Station target);
}
