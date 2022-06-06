package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.Station;
import wooteco.subway.domain.line.Line;

public interface PathCalculator {

    Path calculatePath(Station source, Station target, List<Line> lines);
}
