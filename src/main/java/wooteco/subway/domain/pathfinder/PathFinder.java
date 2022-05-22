package wooteco.subway.domain.pathfinder;

import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

public interface PathFinder {
    Path findShortest(List<Line> lines, Station source, Station target);
}
