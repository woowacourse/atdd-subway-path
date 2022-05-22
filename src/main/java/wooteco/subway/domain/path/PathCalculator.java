package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public interface PathCalculator {
    int calculateShortestDistance(Sections sections, Path path);

    List<Station> calculateShortestStations(Sections sections, Path path);

    List<ShortestPathEdge> findPassedEdges(Sections sections, Path path);
}
