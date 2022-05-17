package wooteco.subway.domain.strategy;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public interface FindPathStrategy {

    Path findPath(Station source, Station target, Sections sections);
}
