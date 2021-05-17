package wooteco.subway.path.domin;

import wooteco.subway.station.domain.Station;

public interface PathRepository {
    Path generateShortestDistancePath(Station start, Station destination);
}
