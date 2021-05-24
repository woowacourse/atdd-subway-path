package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

public interface ShortestPath {
    void resetGraph(Sections sections);

    Path getPath(Station source, Station target);
}
