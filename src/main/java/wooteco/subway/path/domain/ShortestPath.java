package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public interface ShortestPath {
    void resetGraph(List<Section> sections);

    Path getPath(Station source, Station target);
}
