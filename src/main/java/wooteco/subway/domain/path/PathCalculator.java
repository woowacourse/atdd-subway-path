package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public interface PathCalculator {
    int calculateShortestDistance(Sections sections, Station startStation, Station endStation);

    List<Station> calculateShortestStations(Sections sections, Station startStation, Station endStation);

    List<ShortestPathEdge> findPassedEdges(Sections sections, Station startStation, Station endStation);
}
