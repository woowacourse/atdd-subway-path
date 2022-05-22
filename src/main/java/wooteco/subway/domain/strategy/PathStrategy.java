package wooteco.subway.domain.strategy;

import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;

public interface PathStrategy {

    Path calculatePath(Station source, Station target, Sections sections);

}
