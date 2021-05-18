package wooteco.subway.domain.path;

import wooteco.subway.domain.station.Station;

import java.util.List;

public interface BaseShortestPathAlgorithm {

    Path findShortestPath(Station source, Station target);

    List<Station> getShortestPath(Station source, Station target);

    int getShortestDistance(Station source, Station target);
}
