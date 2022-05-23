package wooteco.subway.domain.path;

import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public interface PathFinder {

    Path searchShortestPath(Sections sections, Station source, Station target);
}
