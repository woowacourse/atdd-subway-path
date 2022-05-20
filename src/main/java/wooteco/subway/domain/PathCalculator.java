package wooteco.subway.domain;

import java.util.List;

public interface PathCalculator {
    int calculateShortestDistance(Sections sections, Station startStation, Station endStation);

    List<Station> calculateShortestStations(Sections sections, Station startStation, Station endStation);
}
