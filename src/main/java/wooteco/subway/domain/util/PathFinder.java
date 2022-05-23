package wooteco.subway.domain.util;

import wooteco.subway.domain.Path;

public interface PathFinder {

    Path findShortestPath(Long source, Long target);
}
