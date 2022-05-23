package wooteco.subway.domain.path.factory;

import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Station;

public interface PathFactory {

    Path createShortestPath(Station source, Station target);
}
