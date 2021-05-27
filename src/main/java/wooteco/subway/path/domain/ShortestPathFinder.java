package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.station.domain.Station;

public interface ShortestPathFinder {

    List<Station> findShortestPath(Station from, Station to);

    int findShortestDistance(Station from, Station to);
}
