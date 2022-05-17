package wooteco.subway.domain;

import java.util.List;

public interface PathFinder {

    int calculateDistance(Station from, Station to);

    List<Station> calculatePath(Station from, Station to);
}
