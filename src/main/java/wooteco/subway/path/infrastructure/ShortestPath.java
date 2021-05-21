package wooteco.subway.path.infrastructure;

import java.util.List;

import wooteco.subway.station.domain.Station;

public interface ShortestPath {
    List<Station> getStations();

    int getDistance();
}
