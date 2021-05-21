package wooteco.subway.path.infrastructure;

import java.util.List;

import wooteco.subway.station.domain.Station;

public interface ShortestPath {
    List<Station> getStations(Station source, Station target);

    int getDistance(Station source, Station target);
}
