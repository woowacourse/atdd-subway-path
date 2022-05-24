package wooteco.subway.domain.path;

import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public interface FindPathStrategy {

    Path findPath(Station source, Station target, Sections sections);
}
