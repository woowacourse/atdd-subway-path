package wooteco.subway.path.infrastructure;

import java.util.List;

import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.Path;
import wooteco.subway.station.domain.Station;

public interface ShortestPath {
    Path getPath(Station source, Station target, List<Section> sections);
}
