package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.station.Station;

public interface PathStrategy {
    List<Station> findPath(Station source, Station target);

    int calculateDistance(Station source, Station target);

    List<Long> findLineIds(Station source, Station target);
}
