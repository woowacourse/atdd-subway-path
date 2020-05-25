package wooteco.subway.admin.domain;

import java.util.List;

public interface PathCalculator {

    List<Long> findShortestPath(Station sourceStation, Station targetStation, SearchType searchType,
                                List<Station> allStations, List<LineStation> allLineStations);

    int calculateTotalDistance(List<Station> stationsOnPath);

    int calculateTotalDuration(List<Station> stationsOnPath);
}
