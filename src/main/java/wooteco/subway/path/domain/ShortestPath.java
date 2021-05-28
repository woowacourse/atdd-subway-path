package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public interface ShortestPath {
    List<Station> find(Station source, Station target);

    int calculate(Station source, Station target);
}
