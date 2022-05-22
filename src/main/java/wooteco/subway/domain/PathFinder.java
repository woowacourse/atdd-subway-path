package wooteco.subway.domain;

import java.util.List;

public interface PathFinder {
    Path findShortest(List<Line> lines, Station source, Station target);
}
