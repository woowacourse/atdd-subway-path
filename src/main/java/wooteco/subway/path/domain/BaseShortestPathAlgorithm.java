package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public interface BaseShortestPathAlgorithm {

    Path findShortestPath(Station source, Station target);

    List<Station> getShortestPath(Station source, Station target);

    int getShortestDistance(Station source, Station target);
}
