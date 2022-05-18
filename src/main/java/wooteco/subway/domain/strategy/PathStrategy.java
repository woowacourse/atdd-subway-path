package wooteco.subway.domain.strategy;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public interface PathStrategy {

    Path calculatePath(Station source, Station target, Sections sections);

}
