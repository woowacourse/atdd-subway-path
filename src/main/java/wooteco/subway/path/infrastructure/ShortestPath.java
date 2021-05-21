package wooteco.subway.path.infrastructure;

import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.station.domain.Station;

import java.util.List;

public interface ShortestPath {
    Path getPath(Station source, Station target, List<Section> sections);
}
