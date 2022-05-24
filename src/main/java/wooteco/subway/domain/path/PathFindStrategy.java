package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public interface PathFindStrategy {

    Path findPath(List<Line> lines, Station sourceStation, Station targetStation);
}
