package wooteco.subway.domain;

import java.util.List;

public interface ShortestPathAlgorithm {

    Path findShortestPath(Station source, Station target);

    List<Station> getShortestPath(Station source, Station target);

    int getShortestDistance(Station source, Station target);
}
