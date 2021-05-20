package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public interface SubwayPathStrategy {

    List<Station> shortestPath(Station source, Station target);

    int shortestDistance(Station source, Station target);
}
