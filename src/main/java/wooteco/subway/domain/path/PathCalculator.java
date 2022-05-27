package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.Station;

public interface PathCalculator {

    List<Station> calculateShortestPath(Station source, Station target);

    List<Long> calculateShortestPathLines(Station source, Station target);

    double calculateShortestDistance(Station source, Station target);
}
