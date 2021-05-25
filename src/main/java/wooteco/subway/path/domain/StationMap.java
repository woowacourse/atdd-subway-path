package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

public interface StationMap {

    Path pathOf(Station source, Station target);
}
